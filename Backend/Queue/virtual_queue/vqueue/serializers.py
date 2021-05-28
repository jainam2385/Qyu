from rest_framework import serializers
from .models import Queue


class QueueSerializer(serializers.ModelSerializer):

    class Meta:
        model = Queue
        fields = '__all__'
