from django.urls import path
from .views import (EventDetailApi)

urlpatterns = [
    path('event-detail/', EventDetailApi.as_view(), name='event-detail'),
]
