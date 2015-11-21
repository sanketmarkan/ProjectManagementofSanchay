# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('project_management', '0009_annotator_model_pic'),
    ]

    operations = [
        migrations.AlterField(
            model_name='annotator',
            name='model_pic',
            field=models.ImageField(default=b'static/no-img.jpg', upload_to=b'static/'),
        ),
    ]
