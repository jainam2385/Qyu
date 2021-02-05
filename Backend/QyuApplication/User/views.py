from .serializers import UserDetailSerailizer
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .models import UserDetail
from django.contrib.auth.hashers import (
    make_password, 
    check_password
    )

def hash_password(password):
    return make_password(password)


def confirm_password(password, hashed_password):
    return  check_password(password, hashed_password)


def get_user_model(user_id):
    return UserDetail.objects.get(id=user_id)


class UserDetailApi(APIView):
    
    permission_classes = [IsAdminUser]

    def get(self, request):

        try:
            user_id = request.GET["id"]
            user_model = get_user_model(user_id)
            
            serializer = UserDetailSerailizer(
                user_model, 
                many=False
            )

            return Response(
                serializer.data,
                status=status.HTTP_202_ACCEPTED
            )

        except:
            return Response(
                status=status.HTTP_404_NOT_FOUND,
            )


    def post(self, request):
        user_data = UserDetailSerailizer(data = request.data)
        if user_data.is_valid():
            user_data.validated_data["password"] = hash_password(user_data.validated_data["password"])
            user_data.save()

            return Response(
                status=status.HTTP_201_CREATED,
            )
        
        return Response(
            user_data.errors,
            status=status.HTTP_400_BAD_REQUEST
        )


    def put(self, request):
        user_id = request.GET["id"]
        serializer = UserDetailSerailizer(get_user_model(user_id) ,data=request.data)
        try:
            if serializer.is_valid():
                serializer.save()
                return Response(
                    serializer.data, 
                    status=status.HTTP_201_CREATED
                )

        except:
            return Response(
                serializer.errors, 
                status=status.HTTP_406_NOT_ACCEPTABLE
            )


    def delete(self, request):

        user_id = request.GET["id"]
        try:
            user_model = get_user_model(user_id)
            user_model.delete()
            return Response(
                status=status.HTTP_204_NO_CONTENT
            )

        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )


class AuthenticateUserApi(APIView):

    permission_classes = [IsAdminUser]

    def post(self, request):
        try:
            username_ = request.POST.get("username")
            password_ = request.POST.get("password")
            user_model = UserDetail.objects.get(username=username_)

            if confirm_password(password_, user_model.password):
                return Response({
                    "authenticate": True,
                }, status = status.HTTP_200_OK)
            return Response({
                "authenticate": False,
            }, status=status.HTTP_401_UNAUTHORIZED)
        except:
            return Response(status=status.HTTP_400_BAD_REQUEST)
