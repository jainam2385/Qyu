from rest_framework import serializers
from .models import OrganizationDetail

class OrganizationDetailSerailizer(serializers.ModelSerializer):
    
    class Meta:
        model = OrganizationDetail
        fields = "__all__"
