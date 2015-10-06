# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0003_remove_document_name'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='batch',
            options={'permissions': (('can_moderate', 'Can moderate batches'),)},
        ),
    ]
