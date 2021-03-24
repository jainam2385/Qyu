from django.urls import path
from .views import (
    OrganizationDetailApi,
    AuthenticateOrganizationApi,
    OrganizationEvents,
    AllOrganizations,
    OrganizationStatistics
)


urlpatterns = [
    path('organization-detail/', OrganizationDetailApi.as_view(), name='organization-detail'),
    path('authenticate-organization/', AuthenticateOrganizationApi.as_view(), name='authenticate-organization'),
    path('organization-events/', OrganizationEvents.as_view(), name='organization-events'),
    path('get-all-organizations/', AllOrganizations.as_view(), name='get-all-organizations'),
    path('organization-stats/', OrganizationStatistics.as_view(), name='organization-stats')
]
