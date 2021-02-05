from django.urls import path
from .views import UserDetailApi


urlpatterns = [
    path('get-user-detail/', UserDetailApi.as_view(), name="get-user-detail"),
]
