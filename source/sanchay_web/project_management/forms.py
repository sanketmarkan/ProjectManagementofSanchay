from django import forms
from django.forms.extras.widgets import SelectDateWidget
import datetime
from .models import Annotator


class CreateAnnotatorForm(forms.Form):
    username = forms.CharField(label='Username', max_length=100)
    password = forms.CharField(label='Password', max_length=100,widget = forms.PasswordInput)
    confirm_password = forms.CharField(label='confirm_password', max_length=100,widget = forms.PasswordInput)
    email = forms.EmailField()
    first_name = forms.CharField(label='First Name', max_length=100)
    last_name = forms.CharField(label='Last Name', max_length=100)
    moderator_id = forms.CharField(label='Moderator Id(for moderator account)', max_length=100, initial = 'NORMAL')
    #mobile        = forms.IntegerField(label=(u'Mobile'))
    #last_login    =datetime.datetime.now()
    date_joined   = datetime.datetime.now()





class NewBatchForm(forms.Form):
    name = forms.CharField(label='Name', max_length=100)

class AllotBatchForm(forms.Form):
    batch_id = forms.DecimalField(label = 'Batch ID')
    username = forms.CharField(label='Username of the annotator whom you want to allot', max_length=100)
    deadline = forms.DateField(label='Deadline', widget=SelectDateWidget)
   

class NewDocumentForm(forms.Form):
    batch_id = forms.DecimalField(label = 'Batch ID')
    docfile = forms.FileField(label='Select a file', help_text='max. 50 megabytes')

class NewDocBatchForm(forms.Form):
    docfile = forms.FileField(label='Select a file', help_text='max. 50 megabytes')

class HomeLoginForm(forms.Form):
    username = forms.CharField(label='Username', max_length=100)
    password = forms.CharField(label='Password', max_length=100,widget = forms.PasswordInput)

class AllotUserWithinBatch(forms.Form):
    username = forms.CharField(label='Username', max_length=100)
    deadline = forms.DateField(label='Deadline', widget=SelectDateWidget)


class EditProfileform(forms.Form):
    old = forms.CharField(label='Oldpassword', max_length=100,widget = forms.PasswordInput)
    new = forms.CharField(label='Newpassword', max_length=100,widget = forms.PasswordInput)


class NewMessageForm(forms.Form):
    receiver = forms.CharField(label='To:', max_length=100)
    subject = forms.CharField(label='Subject:', max_length=100)
    msgtext = forms.CharField(label='Message:', max_length=1000, widget=forms.Textarea)


