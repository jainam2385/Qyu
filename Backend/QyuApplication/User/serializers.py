from django.db.models import fields
from rest_framework import serializers
from .models import UserDetail

class UserDetailSerailizer(serializers.ModelSerializer):
    class Meta:
        model = UserDetail
        fields = "__all__"
