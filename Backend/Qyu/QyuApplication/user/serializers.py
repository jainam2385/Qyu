from django.db.models import fields
from rest_framework import serializers
from .models import UserDetail
from django.contrib.auth.models import User


class UserDetailSerializer(serializers.ModelSerializer):
	
	class Meta:
		model = UserDetail
		fields = "__all__"

class UserSerializer(serializers.ModelSerializer):

	class Meta:
		model = User
		fields = (
			'username',
			'email',
			'password',
			'first_name',
			'last_name'
		)