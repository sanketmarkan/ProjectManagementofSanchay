from django.conf.urls import url

from . import views

urlpatterns = [
	url(r'^$', views.index, name='index'),
	url(r'^create_annotator/$', views.create_annotator , name = 'create_annotator'),
	url(r'^user_home/$', views.user_home, name = 'user_home'),
	url(r'^create_batch/$', views.create_batch, name = 'create_batch'),
	url(r'^allot_batch/$', views.allot_batch, name = 'allot_batch'),
	url(r'^upload_file/$', views.upload_file, name = 'upload_file'),
]