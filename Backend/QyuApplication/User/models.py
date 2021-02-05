from django.db import models
from django.db.models.fields import BooleanField

class UserDetail(models.Model):
    username = models.CharField(unique=True, blank=False, max_length=100)
    password = models.CharField(blank=False, max_length=100)
    email = models.EmailField(unique=True, blank=False, max_length=100)
    contact = models.CharField(max_length=10, blank=True, default="")
    is_verified = BooleanField(default=False)

    def __str__(self):
        return f"{self.id}"
