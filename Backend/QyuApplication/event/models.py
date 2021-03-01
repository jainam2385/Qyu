from django.db import models
from organization.models import OrganizationDetail

# Create your models here.

EVENT_STATUS = (
    ('R', 'registration'),
    ('A', 'active'),
    ('D', 'archive')
)

class Event(models.Model):
    name = models.CharField(max_length=100, blank=False)
    organization_id = models.ForeignKey(OrganizationDetail, on_delete=models.CASCADE)
    description = models.CharField(max_length = 1000, blank=False)
    start_date_time = models.DateTimeField(blank=False)
    end_date_time = models.DateTimeField(blank=False)
    max_participants = models.IntegerField(blank=False)
    avg_waiting_time = models.FloatField(blank=False)
    status = models.CharField(max_length = 1, choices = EVENT_STATUS, blank=False)
    security_key = models.CharField(max_length=10,blank=True, unique=True, default='')

    def __str__(self):
        return f"{self.id} - {self.name}"
