from django.shortcuts import render, get_object_or_404, redirect
from django.http import HttpResponse, HttpResponseRedirect, Http404
from django.core.urlresolvers import reverse
from django.utils import timezone
from django.contrib.auth.models import User, Group, Permission
from django.contrib.auth.forms import AuthenticationForm
from .forms import CreateAnnotatorForm, NewBatchForm, AllotBatchForm, NewDocumentForm, NewDocBatchForm, HomeLoginForm, AllotUserWithinBatch, NewMessageForm, EditProfileform
from .forms import ImageUploadForm
from .models import Annotator, Batch, Document, Message, Deadline
from django.contrib.auth.decorators import login_required, permission_required
from django.contrib.auth import authenticate, login
from django.contrib.auth.backends import ModelBackend
import datetime
from django.contrib.auth import logout

from PIL import Image
@login_required
def logout_view(request):
    logout(request)
    return HttpResponseRedirect(reverse('project_management:home'))

def upload_pic(request):
    if request.method == 'POST':
        form = ImageUploadForm(request.POST, request.FILES)
        if form.is_valid():
            m = Annotator.objects.get(user=request.user)
            m.model_pic = form.cleaned_data['image']
            m.save()
            return HttpResponseRedirect(reverse('project_management:view_profile'))
    else:
    	form = ImageUploadForm(request.POST, request.FILES)
    return render(request, 'project_management/upload_pic.html', {'form': form})

def index(request):
	return HttpResponse("Hello, You are at Sanchay Web home page.Site is under construction.")

def create_annotator(request):
    # If a moderators groups does not exist ,create it and add required permissions
	new_obj = False
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
			moderator_id_entry = form.cleaned_data['moderator_id']
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
						user_obj.date_joined = datetime.datetime.now()
 						if moderator_id_entry == 'MODERATOR':
						    print 'Getting moderator'
						    moderator_group = Group.objects.get(name='moderator')
						    print ' Added to moderator group -----<><>-----'
						    user_obj.groups.add(moderator_group)
						user_obj.save()
						annotator_obj = Annotator(user = user_obj, date_created = timezone.now())
						annotator_obj.save()
						new_obj = True
						return render(request, 'project_management/create_annotator.html', {'form': form, 'new_obj':new_obj})
					else:
						form.add_error('password', 'Both the passwords do not match.Enter again.')
			if(email_exists):
				form.add_error('email', 'Given email address already exists')
			if(username_exists):
				form.add_error('username' ,'This username already exists, choose another')
	else:
		form = CreateAnnotatorForm()
	return render(request, 'project_management/create_annotator.html', {'form': form, 'new_obj':new_obj})

@login_required
def edit_profile(request):
	user = User.objects.get(pk=request.user.id)
	form = EditProfileform(request.POST)
	return render(request, 'project_management/edit_profile.html',{'form' : form })

@login_required
def view_profile(request):
	user = User.objects.get(pk=request.user.id)
	anno = Annotator.objects.get(user=user)
	cutime =datetime.datetime.now()
	return render(request, 'project_management/view_profile.html',{'user':user,'anno':anno})

@login_required
def update_profile(request):
	user = User.objects.get(pk=request.user.id)
	if request.method=='POST':
		form = EditProfileform(request.POST)
		if form.is_valid():
			oldpassword = form.cleaned_data['old']
			newpassword = form.cleaned_data['new']
			if user.check_password(oldpassword):
				user.set_password(newpassword)
				user.save()
				return HttpResponseRedirect(reverse('project_management:home'))
			else:
				return render(request,'project_management/edit_profile.html',{'form' : form , 'error_message' : "Oldpassword is not same as the original"})
		else:
			return render(request,'project_management/edit_profile.html',{'form' : form , 'error_message': "Form can't be left empty"})


@login_required
def user_home(request):
	is_moderator = False
	if request.user.has_perm('project_management.can_mod') == True:
		print '------Moderator set-----'
		is_moderator = True
	messages = Message.objects.filter(receiver=request.user.annotator)
	num_messages = messages.count()
	batches = []
	all_deadlines = Deadline.objects.all()
	for deadline in all_deadlines:
		if deadline.approaching_final_date():
			batches.append(deadline)
	num_batches = len(batches)
	context = {'user': request.user, 'is_moderator': is_moderator, 'messages':messages, 'num_messages':num_messages, 'batches':batches, 'num_batches':num_batches}
	return render(request, 'project_management/user_home.html', context)


@permission_required('project_management.can_mod')
@login_required
def create_batch(request):
	new_obj = False
	if request.method == 'POST':
		form = NewBatchForm(request.POST)
		if form.is_valid():
			name_entry = form.cleaned_data['name']
			batch_obj = Batch(name = name_entry, date_created = timezone.now())
			batch_obj.save()
			new_obj = True
			return HttpResponseRedirect(reverse('project_management:view_all_batches'))
			#return render(request, 'project_management/create_batch.html', {'form': form, 'user': request.user, 'new_obj':new_obj})
	else:
		form = NewBatchForm()
	return render(request, 'project_management/create_batch.html', {'form': form, 'user': request.user, 'new_obj':new_obj})

def home(request):
	if request.method == 'POST':
		form = HomeLoginForm(request.POST)
		is_moderator = False
		if form.is_valid():
			username_entry = form.cleaned_data['username']
			password_entry = form.cleaned_data['password']
			user = authenticate(username=username_entry, password=password_entry)
			if user is not None:
				if user.is_active:
					login(request, user)
					if request.user.has_perm('project_management.can_mod') == True:
						print '------Moderator set-----'
						is_moderator = True
					messages = Message.objects.filter(receiver=request.user.annotator)
					num_messages = messages.count()
					batches = []
					all_deadlines = Deadline.objects.all()
					for deadline in all_deadlines:
						if deadline.approaching_final_date():
							batches.append(deadline)
					num_batches = len(batches)
					context = {'user': request.user, 'is_moderator':is_moderator, 'messages':messages, 'num_messages':num_messages, 'batches':batches, 'num_batches':num_batches}
					return render(request, 'project_management/user_home.html', context)
				else:
					form.add_error('username', 'User is not active')
			else:
				form.add_error(None, 'The username/password do not match.')
	else:
		form = HomeLoginForm()
	return render(request, 'project_management/home.html', {'form': form})

@permission_required('project_management.can_mod')
@login_required
def allot_batch(request):
	if request.method == 'POST':
		form = AllotBatchForm(request.POST)
		if form.is_valid():
			batch_id_entry = form.cleaned_data['batch_id']
			username_entry = form.cleaned_data['username']
			deadline_entry = form.cleaned_data['deadline']
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
					deadline_obj = Deadline(final_date=deadline_entry, annotator=allot_annotator, batch=allot_batch)
					deadline_obj.save()
					allot_annotator.save()
					allot_user.save()
					return HttpResponse('Batch allotted to the specified user.')
	else:
		form = AllotBatchForm()
	return render(request, 'project_management/allot_batch.html', {'form': form, 'user': request.user})

@permission_required('project_management.can_mod')
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
				return HttpResponseRedirect(reverse('project_management:view_all_batches'))
				#return HttpResponse('File uploaded.')
	else:
		form = NewDocumentForm()
	return render(request, 'project_management/upload_file.html', {'form': form, 'user': request.user})


@login_required
def view_batches(request):
	batches = request.user.annotator.batches.all()
	return render(request, 'project_management/view_batches.html', {'user': request.user, 'batches':batches})

@login_required
def view_batch_files(request, batch_id):
	new_file = False
	batch = get_object_or_404(Batch, pk=batch_id)
	documents = batch.document_set.all()
	print documents.all()
	return render(request, 'project_management/view_batch_files.html', {'user': request.user, 'documents':documents, 'batch_id':batch_id, 'new_file': new_file})

@permission_required('project_management.can_mod')
@login_required
def upload_file_within_batch(request, batch_id):
	new_file = False
	if request.method == 'POST':
		form = NewDocBatchForm(request.POST, request.FILES)
		batch_obj = get_object_or_404(Batch, pk=batch_id)
		if form.is_valid():
			doc_obj = Document(docfile = request.FILES['docfile'], date_created = timezone.now(), batch = batch_obj)
			doc_obj.save()
			new_file = True
			return HttpResponseRedirect(reverse('project_management:view_all_batches'))
			#return render(request, 'project_management/upload_file_within_batch.html', {'form': form, 'user': request.user, 'batch_id':batch_id, 'new_file':new_file})
	else:
		form = NewDocBatchForm()
	return render(request, 'project_management/upload_file_within_batch.html', {'form': form, 'user': request.user, 'batch_id':batch_id, 'new_file':new_file})
###################################################################
@permission_required('project_management.can_mod')
@login_required
def delete_batch(request,batch_id):
	Batch.objects.get(pk=batch_id).delete()
	# This will delete the Blog and all of its Entry objects.
	batches = Batch.objects.all()
	return HttpResponseRedirect(reverse('project_management:view_all_batches'))
######################################################################

@permission_required('project_management.can_mod')
@login_required
def view_all_batches(request):
	if request.method == 'POST':
		batches = Batch.objects.all()
	else:
		batches = Batch.objects.all()
	return render(request, 'project_management/view_all_batches.html', {'batches':batches})

@permission_required('project_management.can_mod')
@login_required
def allot_user_within_batch(request, batch_id):
	if request.method == 'POST':
		form = AllotUserWithinBatch(request.POST)
		batch_obj = get_object_or_404(Batch, pk=batch_id)
		if form.is_valid():
			username_entry = form.cleaned_data['username']
			deadline_entry = form.cleaned_data['deadline']
			try:
			    allot_batch = Batch.objects.get(pk=batch_id)
			except Exception:
				form.add_error(None, 'Given batch id does not exist')
			else:
				try:
					allot_user = User.objects.get(username = username_entry)
				except Exception:
					form.add_error('username', 'Given user does not exist')
				else:
					allot_annotator = allot_user.annotator
					allot_annotator.batches.add(allot_batch)
					deadline_obj = Deadline(final_date=deadline_entry, annotator=allot_annotator, batch=allot_batch)
					deadline_obj.save()
					allot_annotator.save()
					allot_user.save()
					return HttpResponseRedirect(reverse('project_management:view_all_batches'))
					#return HttpResponse('Batch allotted to the specified user.')
	else:
		form = AllotUserWithinBatch()
	return render(request, 'project_management/allot_user_within_batch.html', {'form': form, 'user': request.user, 'batch_id':batch_id})


@login_required
def new_message(request):
	new_obj = False
	if request.method == 'POST':
		form = NewMessageForm(request.POST)
		if form.is_valid():
			subject_entry = form.cleaned_data['subject']
			msgtext_entry = form.cleaned_data['msgtext']
			receiver_entry = form.cleaned_data['receiver']
			try:
			    receiver_obj = User.objects.get(username=receiver_entry)
			except Exception:
				form.add_error('receiver', 'Given receiver does not exist')
			else:
				receiver_obj = receiver_obj.annotator
				sender_obj = request.user.annotator
				message_obj = Message(subject=subject_entry, msgtext=msgtext_entry, date_created = timezone.now(), sender= sender_obj, receiver= receiver_obj )
				message_obj.save()
				new_obj = True
				render(request, 'project_management/new_message.html', {'form': form, 'user': request.user, 'new_obj':new_obj})
	else:
		form = NewMessageForm()
	return render(request, 'project_management/new_message.html', {'form': form, 'user': request.user, 'new_obj':new_obj})
