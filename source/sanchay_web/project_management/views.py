from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.utils import timezone
from django.contrib.auth.models import User
from .forms import CreateAnnotatorForm, NewBatchForm, AllotBatchForm, NewDocumentForm
from .models import Annotator, Batch, Document
from django.contrib.auth.decorators import login_required

# Create your views here.

def index(request):
	return HttpResponse("Hello, You are at Sanchay Web home page.Site is under construction.")

def create_annotator(request):
	password_mismatch = False
	email_exists = False
	username_exists = False
	if request.method == 'POST':
		form = CreateAnnotatorForm(request.POST)
		if form.is_valid():
			username_entry = form.cleaned_data['username']
			password_entry = form.cleaned_data['password']
			confirm_password_entry = form.cleaned_data['confirm_password']
			email_entry = form.cleaned_data['email']
			first_name_entry = form.cleaned_data['first_name']
			last_name_entry = form.cleaned_data['last_name']
			try:# Checks if the username already exists or not
				username_exists = True
				user_obj = User.objects.get(username = username_entry)
			except Exception:
				user_obj = None
				username_exists = False
				try:#Checks if the email already exists or not 
					email_exists = True
					user_obj = User.objects.get(email = email_entry)
				except Exception:
					user_obj = None
					email_exists = False
					password_mismatch = True
					if password_entry == confirm_password_entry:
						password_mismatch = False
						user_obj = User.objects.create_user(username_entry, email_entry, password_entry)
						user_obj.last_name = last_name_entry
						user_obj.first_name = first_name_entry
						user_obj.save()
						annotator_obj = Annotator(user = user_obj, date_created = timezone.now())
						annotator_obj.save()
						return HttpResponse('New annotator created')
					else:
						form.add_error('password', 'Both the passwords do not match.Enter again.')
			if(email_exists):
				form.add_error('email', 'Given email address already exists')
			if(username_exists):
				form.add_error('username' ,'This username already exists, choose another')
	else:
		form = CreateAnnotatorForm()
	return render(request, 'project_management/create_annotator.html', {'form': form})


@login_required
def user_home(request):
	context = {'user': request.user}
	return render(request, 'project_management/user_home.html', context)

@login_required
def create_batch(request):
	if request.method == 'POST':
		form = NewBatchForm(request.POST)
		if form.is_valid():
			name_entry = form.cleaned_data['name']
			batch_obj = Batch(name = name_entry, date_created = timezone.now())
			batch_obj.save()
			return HttpResponse('New batch created')
	else:
		form = NewBatchForm()
	return render(request, 'project_management/create_batch.html', {'form': form, 'user': request.user})

@login_required
def allot_batch(request):
	if request.method == 'POST':
		form = AllotBatchForm(request.POST)
		if form.is_valid():
			batch_id_entry = form.cleaned_data['batch_id']
			username_entry = form.cleaned_data['username']
			try:
			    allot_batch = Batch.objects.get(pk=batch_id_entry)
			except Exception:
				form.add_error('batch_id', 'Given batch id does not exist')
			else:
				try:
					allot_user = User.objects.get(username = username_entry)
				except Exception:
					form.add_error('username', 'Given user does not exist')
				else:
					allot_annotator = allot_user.annotator
					allot_annotator.batches.add(allot_batch)
					return HttpResponse('Batch allotted to the specified user.')
	else:
		form = AllotBatchForm()
	return render(request, 'project_management/allot_batch.html', {'form': form, 'user': request.user})

@login_required
def upload_file(request):
	if request.method == 'POST':
		form = NewDocumentForm(request.POST, request.FILES)
		if form.is_valid():
			batch_id_entry = form.cleaned_data['batch_id']
			try:
			    batch_obj = Batch.objects.get(pk = batch_id_entry)
			except Exception:
				form.add_error('batch_id', 'A batch of given id does not exist')
			else:
				doc_obj = Document(docfile = request.FILES['docfile'], date_created = timezone.now(), batch = batch_obj)
				doc_obj.save()
				return HttpResponse('File uploaded.')
	else:
		form = NewDocumentForm()
	return render(request, 'project_management/upload_file.html', {'form': form, 'user': request.user})


	