from rest_framework import serializers
from .models import ReviewOrganization


class OrganizationReviewSerializer(serializers.ModelSerializer):
    class Meta:
        model = ReviewOrganization
        fields = "__all__"
