"""virtual_queue URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.urls.conf import include
from rest_framework.authtoken import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api-token-auth/', views.obtain_auth_token, name='api-token-auth'),
    path('userapi/', include('User.urls'), name='user-api'),
    path('organizationapi/', include('organization.urls'), name='organization-api'),
    path('subscriptionapi/', include('subscription.urls'), name='subscription-api'),
    path('eventapi/', include('event.urls'), name='event-api'),
    path('queueapi/', include('vqueue.urls'), name='virtual-queue-api'),
    path('reviewapi/', include('review.urls'), name='review-api'),
]
