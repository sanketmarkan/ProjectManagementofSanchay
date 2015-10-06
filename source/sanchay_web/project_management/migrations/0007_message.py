# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0006_auto_20151006_2117'),
    ]

    operations = [
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('subject', models.CharField(max_length=100)),
                ('msgtext', models.CharField(max_length=1000)),
                ('date_created', models.DateTimeField(verbose_name=b'date created')),
                ('receiver', models.ForeignKey(related_name='receiver', to='project_management.Annotator')),
                ('sender', models.ForeignKey(related_name='sender', to='project_management.Annotator')),
            ],
        ),
    ]
