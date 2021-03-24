from .serializers  import OrganizationDetailSerailizer
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .models import OrganizationDetail
from django.contrib.auth.hashers import (
    make_password,
    check_password
    )
from event.models import Event
from event.serializers import EventSerializer
from vqueue.models import Queue

def hash_password(password):
    return make_password(password)


def confirm_password(password, hashed_password):
    return check_password(password, hashed_password)


def get_organization_model(organization_email):
    return OrganizationDetail.objects.get(email=organization_email)


class OrganizationDetailApi(APIView):

    permission_classes = [IsAdminUser]

    def get(self, request):
        try:
            organization_email = request.GET["email"]
            organization_model = get_organization_model(organization_email)
            serializer = OrganizationDetailSerailizer(
                organization_model,
                many=False
            )
            return Response(
                serializer.data,
                status=status.HTTP_202_ACCEPTED
            )

        except:
            return Response(
                status=status.HTTP_404_NOT_FOUND,
            )


    def post(self, request):
        organization_data = OrganizationDetailSerailizer(data = request.data)
        if organization_data.is_valid():
            organization_data.validated_data["password"] = hash_password(organization_data.validated_data["password"])
            organization_data.save()

            return Response(
                status=status.HTTP_201_CREATED,
            )

        return Response(
            organization_data.errors,
            status=status.HTTP_400_BAD_REQUEST
        )


    def put(self, request):
        organization_email = request.GET["email"]
        serializer = OrganizationDetailSerailizer(
            get_organization_model(organization_email),
            data=request.data
        )

        try:
            if serializer.is_valid():
                serializer.save()
                return Response(
                    serializer.data,
                    status=status.HTTP_201_CREATED
                )

        except:
            return Response(
                serializer.errors,
                status=status.HTTP_406_NOT_ACCEPTABLE
            )

    def delete(self, request):
        organization_email = request.GET["email"]
        try:
            organization_model = get_organization_model(organization_email)
            organization_model.delete()
            return Response(
                status=status.HTTP_204_NO_CONTENT
            )

        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )


class AuthenticateOrganizationApi(APIView):

    permission_classes = [IsAdminUser]

    def post(self, request):
        try:
            email = request.POST.get('email')
            password_ = request.POST.get('password')
            organization_model = get_organization_model(email)
            serializer = OrganizationDetailSerailizer(
                organization_model,
                many=False
            )

            if confirm_password(password_, organization_model.password):
                return Response(
                    serializer.data,
                    status = status.HTTP_200_OK
                )

            return  Response({
                "authenticate": False
            }, status = status.HTTP_401_UNAUTHORIZED)
        except:
            return Response(status=status.HTTP_400_BAD_REQUEST)


class OrganizationEvents(APIView):

    permission_classes = [IsAdminUser]

    def get(self, request):
        try:
            status_ = None
            if "status" in request.GET:
                status_ = request.GET['status']

            organization_id_ = request.GET["organization_id"]
            if status_ == "A" or status_ == "R" or status_ == "D":
                events = Event.objects.filter(organization_id = organization_id_, status = status_)
            else:
                events = Event.objects.filter(organization_id = organization_id_)

            serializer = EventSerializer(
                events,
                many=True
            )
            return Response(
                serializer.data,
                status = status.HTTP_200_OK
            )
        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )


class AllOrganizations(APIView):
    permission_classes = [IsAdminUser]

    def get(self, request):
        organizations = OrganizationDetail.objects.all()
        organizations_serializer = OrganizationDetailSerailizer(
            organizations,
            many = True
        )
        return Response(
            organizations_serializer.data, 
            status = status.HTTP_200_OK
        )

class OrganizationStatistics(APIView):
    permission_classes = [IsAdminUser]

    def get(self, request):
        try:
            _organization_id = request.GET["organization_id"]
        except:
            return Response({
                "success": False
                }, 
                status=status.HTTP_400_BAD_REQUEST
            )

        try:

            events = Event.objects.filter(organization_id = _organization_id)
            queue = Queue.objects.filter(event_id__in = [current_event.id for current_event in events])
            
            left = queue.filter(status="L").count()
            waiting = queue.filter(status="W").count()
            removed = queue.filter(status="R").count()
            complete = queue.filter(status="C").count()
            reg_events = events.filter(status="R").count()
            ongoing_events = events.filter(status="A").count()
            archive_events = events.filter(status="D").count()

            return Response({
                "left": left,
                "waiting": waiting,
                "removed": removed,
                "complete": complete,
                "reg_event": reg_events,
                "ongoing_event": ongoing_events,
                "archive_event": archive_events,
                "success": True
            })
        except:
            return Response({
                "success": False
                }, status=status.HTTP_400_BAD_REQUEST
            )
