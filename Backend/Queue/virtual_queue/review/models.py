from django.db import models
from User.models import UserDetail
from organization.models import OrganizationDetail

# Create your models here.


class ReviewOrganization(models.Model):
    user_id = models.ForeignKey(UserDetail, on_delete=models.CASCADE)
    organization_id = models.ForeignKey(
        OrganizationDetail, on_delete=models.CASCADE)
    timestamp = models.DateTimeField(auto_now_add=True)
    rating = models.FloatField(blank=False)
    message = models.CharField(max_length=100, default="", blank=True)

    class Meta:
        unique_together = ('user_id', 'organization_id')

    def __str__(self):
        return f"{self.user_id} - {self.organization_id} - {self.rating}"
