# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0005_auto_20151006_2059'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='batch',
            options={'permissions': (('can_mod', ' Can moderate'),)},
        ),
    ]
