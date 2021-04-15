from django.urls import path
from .views import (
    SubscriptionDetailApi,
    BroadcastMeassage,
    OrganizationsSubscribed,
    OrganizationsUnSubscribed
)

urlpatterns = [
    path('subscription-detail/', SubscriptionDetailApi.as_view(), name='subscription-detail'),
    path('broadcast-message/', BroadcastMeassage.as_view(), name='broadcast-message-api'),
    path('organization-subscribed/', OrganizationsSubscribed.as_view(), name='organizations-subscribed'),
    path('organization-unsubscribed/', OrganizationsUnSubscribed.as_view(), name='organization-unsubscribed'),
]
