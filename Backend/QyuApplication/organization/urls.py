from django.urls import path
from .views import (
    OrganizationDetailApi,
    AuthenticateOrganizationApi,
    OrganizationEvents
)


urlpatterns = [
    path('organization-detail/', OrganizationDetailApi.as_view(), name='organization-detail'),
    path('authenticate-organization/', AuthenticateOrganizationApi.as_view(), name='authenticate-organization'),
    path('organization-events/', OrganizationEvents.as_view(), name='organization-events'),
]
