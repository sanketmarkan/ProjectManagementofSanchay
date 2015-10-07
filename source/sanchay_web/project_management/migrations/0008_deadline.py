# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0007_message'),
    ]

    operations = [
        migrations.CreateModel(
            name='Deadline',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('final_date', models.DateField(verbose_name=b'date created')),
                ('annotator', models.ForeignKey(to='project_management.Annotator')),
                ('batch', models.ForeignKey(to='project_management.Batch')),
            ],
        ),
    ]
