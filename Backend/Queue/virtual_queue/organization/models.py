from django.db import models


class OrganizationDetail(models.Model):
    email = models.EmailField(unique=True, blank=False, max_length=100)
    password = models.CharField(blank=False, max_length=100)
    name = models.CharField(max_length=100, blank=False)
    address = models.CharField(max_length=500, blank=False)
    contact = models.CharField(max_length=10, blank=True, default="")
    rating = models.FloatField(default=10.0)
    is_verified = models.BooleanField(default=False)
    gstno = models.CharField(default="", max_length=15, blank=False, unique=True)

    def __str__(self) -> str:
        return f"{self.id}"
