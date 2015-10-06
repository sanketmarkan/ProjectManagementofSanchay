## To set the server



## After setting up everything
cd ...../sanchay_web/
type python manage.py shell
enter the following lines
 moderator_group = Group.objects.create(name='moderator')
 permission = Permission.objects.get(codename='can_mod')
 moderator_group.permissions.add(permission)
 moderator_group.save()
