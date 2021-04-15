from django.urls import path
from .views import (
    EventDetailApi,
    StartEvent,
    EndEvent,
    PublicEvents
)

urlpatterns = [
    path('event-detail/', EventDetailApi.as_view(), name='event-detail'),
    path('start-event/', StartEvent.as_view(), name='start-event'),
    path('end-event/', EndEvent.as_view(), name='end-event'),
    path('public-events/', PublicEvents.as_view(), name="public-events")
]
