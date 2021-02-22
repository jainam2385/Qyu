from django.urls import path
from .views import (
    SubscriptionDetailApi,
    BroadcastMeassage
    )

urlpatterns = [
    path('subscription-detail/', SubscriptionDetailApi.as_view(), name='subscription-detail'),
    path('broadcast-message/', BroadcastMeassage.as_view(), name='broadcast-message-api'),
]
