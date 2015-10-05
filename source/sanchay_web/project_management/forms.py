from django import forms

class CreateAnnotatorForm(forms.Form):
    username = forms.CharField(label='Username', max_length=100)
    password = forms.CharField(label='Password', max_length=100,widget = forms.PasswordInput)
    confirm_password = forms.CharField(label='confirm_password', max_length=100,widget = forms.PasswordInput)
    email = forms.EmailField()
    first_name = forms.CharField(label='First Name', max_length=100)
    last_name = forms.CharField(label='Last Name', max_length=100)


class NewBatchForm(forms.Form):
	name = forms.CharField(label='Name', max_length=100)

class AllotBatchForm(forms.Form):
	batch_id = forms.DecimalField(label = 'Batch ID')
	username = forms.CharField(label='Username of the annotator whom you want to allot', max_length=100)

class NewDocumentForm(forms.Form):
    batch_id = forms.DecimalField(label = 'Batch ID')
    docfile = forms.FileField(label='Select a file', help_text='max. 50 megabytes')

class NewDocBatchForm(forms.Form):
    docfile = forms.FileField(label='Select a file', help_text='max. 50 megabytes')

class HomeLoginForm(forms.Form):
    username = forms.CharField(label='Username', max_length=100)
    password = forms.CharField(label='Password', max_length=100,widget = forms.PasswordInput)

	


