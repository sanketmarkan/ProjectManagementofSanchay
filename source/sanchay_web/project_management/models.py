import os
from django.db import models
from django.contrib.auth.models import User
from django.utils import timezone
import datetime


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


class Deadline(models.Model):
    final_date = models.DateField('date created')
    annotator = models.ForeignKey(Annotator)
    batch = models.ForeignKey(Batch)

    def approaching_final_date(self):
        return self.final_date <= datetime.date.today() + datetime.timedelta(days=5)

