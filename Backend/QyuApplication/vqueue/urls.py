from django.urls import path
from .views import QueueDetailApi


urlpatterns = [
    path('queue-detail/', QueueDetailApi.as_view(), name='queue-detail'),
]