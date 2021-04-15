from django.db import models
from User.models import UserDetail
from event.models import Event

USER_STATUS = (
    ('W', 'waiting'),
    ('L', 'left'),
    ('R', 'removed'),
    ('C', 'complete')
)


class Queue(models.Model):
    user_id = models.ForeignKey(UserDetail, on_delete=models.CASCADE)
    event_id = models.ForeignKey(Event, on_delete=models.CASCADE)
    join_datetime = models.DateTimeField(auto_now_add=True)
    status = models.CharField(max_length=1, choices=USER_STATUS, default='W')

    class Meta:
        unique_together = ('user_id', 'event_id')

    def __str__(self):
        return f"{self.user_id} - {self.event_id}"
