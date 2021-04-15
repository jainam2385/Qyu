from django.urls import path
from .views import (
    QueueDetailApi,
    UserPosition,
    WaitingUsers,
    JoinPrivateQueue,
    UserEventLogs
)


urlpatterns = [
    path('queue-detail/', QueueDetailApi.as_view(), name='queue-detail'),
    path('current-position/', UserPosition.as_view(), name='user-current-position'), 
    path('waiting-users/', WaitingUsers.as_view(), name='waiting-users'),
    path('private-event/', JoinPrivateQueue.as_view(), name="join-private-event"),
    path('user-event-logs/', UserEventLogs.as_view(), name="user-event-logs"),
]
