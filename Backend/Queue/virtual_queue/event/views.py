from .serializers import EventSerializer
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .models import Event
from datetime import datetime
from vqueue.models import Queue
from django.utils import timezone
from django.core.mail import send_mail
from django.core.mail import send_mass_mail
from virtual_queue.settings import EMAIL_HOST_USER
import uuid


def is_unique(key):
    try:
        Event.objects.get(security_key=key)
        return False
    except:
        return True


def generate_security_key():
    is_generated = False
    security_key = '0'*10

    while not is_generated:
        security_key = str(uuid.uuid4())
        security_key = security_key.upper()
        security_key = security_key.replace("-", "")
        security_key = security_key[:10]
        if is_unique(security_key):
            is_generated = True

    return security_key


def get_event_model(event_id):
    return Event.objects.get(id=event_id)


class EventDetailApi(APIView):

    permission_classes = [IsAdminUser]

    def get(self, request):

        try:
            event_id = request.GET["event_id"]
            event_model = Event.objects.get(id=event_id)

            serializer = EventSerializer(
                event_model,
                many=False
            )

            return Response(
                serializer.data,
                status=status.HTTP_202_ACCEPTED
            )
        except:
            return Response(
                status=status.HTTP_404_NOT_FOUND
            )

    def post(self, request):
        event_data = EventSerializer(data=request.data)

        if event_data.is_valid():
            event_data.save(
                security_key=generate_security_key()
            )
            return Response(
                status=status.HTTP_201_CREATED,
            )
        return Response(
            event_data.errors,
            status=status.HTTP_400_BAD_REQUEST
        )

    def put(self, request):
        event_id = request.GET["event_id"]
        serializer = EventSerializer(
            get_event_model(event_id),
            many=False
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
        try:
            event_id = request.GET["event_id"]
            event_model = get_event_model(event_id)
            event_model.status = "D"
            event_model.save()

            return Response(
                EventSerializer(event_model).data,
                status=status.HTTP_200_OK
            )

        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )


class StartEvent(APIView):

    permission_classes = [IsAdminUser]

    def post(self, request):
        try:
            _event_id = request.POST["event_id"]
            event_model = get_event_model(_event_id)

            if event_model.status == "R":
                event_model.status = "A"
                event_model.start_date_time = timezone.now()
                event_model.save()

                users = Queue.objects.filter(event_id=_event_id)
                email_ids = [u.user_id.email for u in users]

                # TODO Starting mail template edit
                subject = "Event started"
                message = "Event started"

                messages = [(subject, message, EMAIL_HOST_USER, [recipient])
                            for recipient in email_ids]

                try:
                    send_mass_mail(messages)
                except:
                    pass

                return Response(
                    status=status.HTTP_200_OK
                )
            else:
                return Response(
                    status=status.HTTP_406_NOT_ACCEPTABLE
                )
        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )


class EndEvent(APIView):

    permission_classes = [IsAdminUser]

    def post(self, request):
        try:
            _event_id = request.POST["event_id"]
            event_model = get_event_model(_event_id)

            # If users are waiting then event cannot end

            if Queue.objects.filter(event_id=_event_id, status="W"):
                return Response({
                    "success": False,
                    "error": "Users are waiting in the queue."
                }, status=status.HTTP_400_BAD_REQUEST)

            if event_model.status == "A":
                event_model.status = "D"
                event_model.end_date_time = timezone.now()
                event_model.save()

                # TODO MAIL USERS OF EVENT ENDING
                users = Queue.objects.filter(event_id=_event_id)
                email_ids = [u.user_id.email for u in users]

                subject = "Event ending"
                message = "Event ending"

                messages = [(subject, message, EMAIL_HOST_USER, [recipient])
                            for recipient in email_ids]

                try:
                    send_mass_mail(messages)
                except:
                    pass

                return Response(
                    status=status.HTTP_200_OK
                )
            else:
                return Response(
                    status=status.HTTP_406_NOT_ACCEPTABLE
                )
        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )


class PublicEvents(APIView):

    permission_classes = [IsAdminUser]

    def get(self, request):
        try:
            _status = request.GET["status"]
            ongoing_events = Event.objects.filter(
                status=_status, is_private=False)
            serializer = EventSerializer(
                ongoing_events,
                many=True
            )
            return Response(
                serializer.data,
                status=status.HTTP_200_OK
            )

        except:
            return Response(
                status=status.HTTP_400_BAD_REQUEST
            )
