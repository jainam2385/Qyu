from django.db import models
from django.contrib.auth.models import User

# Create your models here.

class UserDetail(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    is_verified = models.BooleanField(default=False)
    contact = models.CharField(max_length=11)

    def __str__(self):
        return f"{self.id}"