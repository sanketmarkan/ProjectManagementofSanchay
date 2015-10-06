from django.conf.urls import url, include
from . import views

urlpatterns = [
	url(r'^$', views.index, name='index'),
	url(r'^create_annotator/$', views.create_annotator , name = 'create_annotator'),
	url(r'^home/$', views.home, name = 'home'),
	url(r'^user_home/$', views.user_home, name = 'user_home'),
	url(r'^edit_profile/$', views.edit_profile, name = 'edit_profile'),
	url(r'^update_profile/$', views.update_profile, name = 'update_profile'),
	url(r'^create_batch/$', views.create_batch, name = 'create_batch'),
	url(r'^allot_batch/$', views.allot_batch, name = 'allot_batch'),
	url(r'^upload_file/$', views.upload_file, name = 'upload_file'),
	url(r'^view_batches/$', views.view_batches, name = 'view_batches'),
	url(r'^view_all_batches/$', views.view_all_batches, name = 'view_all_batches'),
	url(r'^(?P<batch_id>[0-9]+)/batch/$', views.view_batch_files, name='view_batch_files'),
	url(r'^(?P<batch_id>[0-9]+)/upload_file/$', views.upload_file_within_batch, name='upload_file_within_batch'),
	url(r'^(?P<batch_id>[0-9]+)/allot_user_within_batch/$', views.allot_user_within_batch, name='allot_user_within_batch'),
	url(r'^view_profile/$', views.view_profile, name = 'view_profile'),
]
