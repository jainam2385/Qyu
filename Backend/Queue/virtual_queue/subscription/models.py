from django.db import models
from organization.models import OrganizationDetail
from User.models import UserDetail

class Subscription(models.Model):
    organization_id = models.ForeignKey(OrganizationDetail, on_delete=models.CASCADE)
    user_id = models.ForeignKey(UserDetail, on_delete=models.CASCADE)

    class Meta:
        unique_together = ('organization_id', 'user_id')

    def __str__(self) -> str:
        return f"{self.organization_id} - {self.user_id}"
