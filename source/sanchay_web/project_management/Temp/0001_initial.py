# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
from django.conf import settings
import project_management.models


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Annotator',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, help_text='', verbose_name='ID', primary_key=True)),
                ('date_created', models.DateTimeField(help_text='', verbose_name=b'date joined')),
            ],
        ),
        migrations.CreateModel(
            name='Batch',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, help_text='', verbose_name='ID', primary_key=True)),
                ('name', models.CharField(max_length=200, help_text='')),
                ('date_created', models.DateTimeField(help_text='', verbose_name=b'date created')),
                ('status', models.CharField(max_length=25, help_text='')),
            ],
            options={
                'permissions': (('can_mod', ' Can moderate'),),
            },
        ),
        migrations.CreateModel(
            name='Deadline',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, help_text='', verbose_name='ID', primary_key=True)),
                ('final_date', models.DateField(help_text='', verbose_name=b'date created')),
                ('annotator', models.ForeignKey(help_text='', to='project_management.Annotator')),
                ('batch', models.ForeignKey(help_text='', to='project_management.Batch')),
            ],
        ),
        migrations.CreateModel(
            name='Document',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, help_text='', verbose_name='ID', primary_key=True)),
                ('docfile', models.FileField(help_text='', upload_to=project_management.models.get_doc_path)),
                ('date_created', models.DateTimeField(help_text='', verbose_name=b'date created')),
                ('status', models.CharField(max_length=25, help_text='')),
                ('batch', models.ForeignKey(help_text='', to='project_management.Batch')),
            ],
        ),
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, help_text='', verbose_name='ID', primary_key=True)),
                ('subject', models.CharField(max_length=100, help_text='')),
                ('msgtext', models.CharField(max_length=1000, help_text='')),
                ('date_created', models.DateTimeField(help_text='', verbose_name=b'date created')),
                ('receiver', models.ForeignKey(related_name='receiver', help_text='', to='project_management.Annotator')),
                ('sender', models.ForeignKey(related_name='sender', help_text='', to='project_management.Annotator')),
            ],
        ),
        migrations.AddField(
            model_name='annotator',
            name='batches',
            field=models.ManyToManyField(help_text='', to='project_management.Batch'),
        ),
        migrations.AddField(
            model_name='annotator',
            name='user',
            field=models.OneToOneField(help_text='', to=settings.AUTH_USER_MODEL),
        ),
    ]
