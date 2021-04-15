from django.urls import path
from .views import (
    ReviewDetailApi,
    ListUserOrganizations,
    ReviewList
)


urlpatterns = [
    path('review-detail/', ReviewDetailApi.as_view(), name="review-detail"),
    path('list-user-organizations/', ListUserOrganizations.as_view(), name="list-user-organizations"),
    path('review-list/', ReviewList.as_view(), name="review-list"),
]
