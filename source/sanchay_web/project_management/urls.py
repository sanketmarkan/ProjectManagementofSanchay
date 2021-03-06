from django.conf.urls import url, include
from . import views

urlpatterns = [
	url(r'^$', views.home, name='home'),
	url(r'^create_annotator/$', views.create_annotator , name = 'create_annotator'),
	url(r'^home/$', views.home, name = 'home'),
	url(r'^user_home/$', views.user_home, name = 'user_home'),
	url(r'^edit_profile/$', views.edit_profile, name = 'edit_profile'),
	url(r'^update_profile/$', views.update_profile, name = 'update_profile'),
	url(r'^create_batch/$', views.create_batch, name = 'create_batch'),
	url(r'^allot_batch/$', views.allot_batch, name = 'allot_batch'),
	url(r'^upload_file/$', views.upload_file, name = 'upload_file'),
	#url(r'^upload_pic$', views.upload_pic, name = 'upload_pic'),
	url(r'^view_batches/$', views.view_batches, name = 'view_batches'),
	url(r'^new_message/$', views.new_message, name = 'new_message'),
	url(r'^(?P<message_id>[0-9]+)/view_message/$', views.view_message, name='view_message'),
	url(r'^view_all_batches/$', views.view_all_batches, name = 'view_all_batches'),

	url(r'^(?P<batch_id>[0-9]+)/delete_batch/$', views.delete_batch, name = 'delete_batch'),
	url(r'^(?P<message_id>[0-9]+)/delete_message/$', views.delete_message, name = 'delete_message'),
	url(r'^(?P<batch_id>[0-9]+)/batch/$', views.view_batch_files, name='view_batch_files'),
	url(r'^(?P<batch_id>[0-9]+)/upload_file/$', views.upload_file_within_batch, name='upload_file_within_batch'),
	url(r'^(?P<batch_id>[0-9]+)/allot_user_within_batch/$', views.allot_user_within_batch, name='allot_user_within_batch'),
	url(r'^view_profile/$', views.view_profile, name = 'view_profile'),
	url(r'^logout_view/$',views.logout_view,name = 'logout_view'),
]
