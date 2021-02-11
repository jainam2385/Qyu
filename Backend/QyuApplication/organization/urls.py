from django.urls import path
from .views import (
    OrganizationDetailApi, 
    AuthenticateOrganizationApi
)


urlpatterns = [
    path('organization-detail/', OrganizationDetailApi.as_view(), name='organizatiton-detail'),
    path('authenticate-organization/', AuthenticateOrganizationApi.as_view(), name='authenticate-organization'),
]
