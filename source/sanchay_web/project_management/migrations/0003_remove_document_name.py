# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0002_annotator_batches'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='document',
            name='name',
        ),
    ]
