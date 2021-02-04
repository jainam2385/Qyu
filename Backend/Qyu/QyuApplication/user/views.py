from collections import UserString
from rest_framework.views import APIView
from rest_framework.response import Response
from .serializers import *
from rest_framework import status
from .models import UserDetail
from rest_framework.permissions import IsAdminUser
from django.contrib.auth.models import User

def get_user_detail(user_id):
    return UserDetail.objects.get(id=user_id)

class UserApi(APIView):
    permission_classes = [IsAdminUser]

    def get(self, request):
        id = request.GET["id"]
        try:
            user_model = get_user_detail(id)
            user_data = user_model.user

            serializer = UserDetailSerializer(
                user_model,
                many=False
            ).data

            for k, v in UserSerializer(user_data, many=False).data.items():
                serializer[k] = v
            return Response(serializer)

        except:
            return Response({
                "error": True,
            },status=status.HTTP_404_NOT_FOUND)

    def post(self, request):
        user_data = UserSerializer(data = request.data)
        if user_data.is_valid():
            print(user_data) 
        return Response(status=status.HTTP_200_OK)
