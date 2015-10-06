import os
from django.db import models
from django.contrib.auth.models import User


class Batch(models.Model):
    name = models.CharField(max_length=200)
    date_created = models.DateTimeField('date created')
    status = models.BooleanField
    class Meta:
        permissions = (("can_mod", " Can moderate"),)
    def __unicode__(self):
        return self.name


class Annotator(models.Model):
    user = models.OneToOneField(User)
    batches = models.ManyToManyField(Batch)
    date_created = models.DateTimeField('date joined')
    #profilepic = models.ImageField(upload_to="profilepics/", default="blabla.jpg")
    #avatar = AvatarField(upload_to='avatars', width=100, height=100, default="https://accounts.google.com/SignOutOptions?hl=en&continue=https://www.google.co.in/")
    

    def __unicode__(self):
        return self.user.username

def get_doc_path(instance, filename):
	return os.path.join(
		"Batch_%d" % instance.batch.id, filename)

class Document(models.Model):
	docfile = models.FileField(upload_to = get_doc_path)
	batch = models.ForeignKey(Batch)
	date_created = models.DateTimeField('date created')
	status = models.BooleanField

	def __unicode__(self):
		return self.docfile.name

class Message(models.Model):
    subject = models.CharField(max_length=100)
    msgtext = models.CharField(max_length=1000)
    date_created = models.DateTimeField('date created')
    sender = models.ForeignKey(Annotator, related_name='sender')
    receiver = models.ForeignKey(Annotator, related_name='receiver')

    def __unicode__(self):
        return self.subject
 