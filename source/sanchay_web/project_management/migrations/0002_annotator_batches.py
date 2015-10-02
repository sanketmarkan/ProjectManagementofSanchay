# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='annotator',
            name='batches',
            field=models.ManyToManyField(to='project_management.Batch'),
        ),
    ]
