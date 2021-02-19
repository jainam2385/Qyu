from django.urls import path
from .views import (
    SubscriptionDetailApi
    )

urlpatterns = [
    path('subscription-detail/', SubscriptionDetailApi.as_view(), name='subscription-detail'),
]
