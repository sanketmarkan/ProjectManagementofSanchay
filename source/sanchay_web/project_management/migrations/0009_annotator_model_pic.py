# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0008_deadline'),
    ]

    operations = [
        migrations.AddField(
            model_name='annotator',
            name='model_pic',
            field=models.ImageField(default=1, upload_to=b'static/'),
            preserve_default=False,
        ),
    ]
