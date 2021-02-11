from django.urls import path
from .views import (
    UserDetailApi, 
    AuthenticateUserApi
)


urlpatterns = [
    path('user-detail/', UserDetailApi.as_view(), name="get-user-detail"),
    path('authenticate-user/', AuthenticateUserApi.as_view(), name="authenticate-user")
]
