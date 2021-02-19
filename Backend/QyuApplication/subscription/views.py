from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .serializers import SubscriptionSerializer
from .models import Subscription

class SubscriptionDetailApi(APIView):
    permission_classes = [IsAdminUser]

    def post(self, request):
        subscription_data = SubscriptionSerializer(data=request.data)
        if subscription_data.is_valid():
            subscription_data.save()
            return Response(
                status=status.HTTP_201_CREATED,
            )
        return Response(
            subscription_data.errors,
            status=status.HTTP_400_BAD_REQUEST
        )


    def delete(self, request):
        try:
            _user_id = request.GET["user_id"]
            _organization_id = request.GET["organization_id"]
            
            subscription_model = Subscription.objects.get(user_id=_user_id, organization_id=_organization_id)
            subscription_model.delete()
            return Response(
                status=status.HTTP_204_NO_CONTENT
            )
        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )
