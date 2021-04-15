from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .models import Queue
from .serializers import QueueSerializer
from User.models import UserDetail
from django.core.mail import send_mail
from virtual_queue.settings import EMAIL_HOST_USER
from event.models import Event
from event.serializers import EventSerializer
from django.core import mail
from django.template.loader import render_to_string
from django.utils.html import strip_tags



def get_queue_model(queue_id):
    return Queue.objects.get(id=queue_id)


def add_new_participant(_event_id):
    event = Event.objects.get(id=_event_id)
    participants = Queue.objects.filter(event_id = _event_id).count()
    return int(event.max_participants) > int(participants)


class QueueDetailApi(APIView):

    permission_classes = [IsAdminUser]

    def __mail(self, user_id, event_id, status):
        try:
            user_model = UserDetail.objects.get(id=user_id)
            event_model = Event.objects.get(id = event_id)

            event_types = {
                "D": "Archived",
                "A": "Active",
                "R": "Registration"
            }

            notification = {
                "left": "We see you have left the queue. Your place in the queue won't be reserved anymore.",
                "removed": "Uh oh it seems like you have been removed from the queue. Make sure you follow our guidelines.",
                "complete": "We hope you had a good experience.\nDon't forget to rate the organization.",
            }

            data = {
                'user': user_model.first_name.capitalize() + " " + user_model.last_name.capitalize(),
                'organization':  event_model.organization_id.name,
                'organization_address': event_model.organization_id.address,
                'organization_contact': event_model.organization_id.contact if event_model.organization_id.contact else "NA",
                'organization_rating': event_model.organization_id.rating,
                'event_name': event_model.name,
                'event_description': event_model.description,
                'event_status': event_types[event_model.status],
                'event_max_participants': event_model.max_participants,
            }

            if status == 'L':
                subject = f'Left Queue of {event_model.name}'

                html_message = render_to_string('notification.html', {**data, "notification": notification["left"]})

                plain_message = strip_tags(html_message)
                from_email = f'Left Queue of {event_model.name}'
                to = user_model.email

                mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)

            elif status == 'R':
                subject = f"Removed from the Queue of {event_model.name}"

                html_message = render_to_string('notification.html', {**data, "notification": notification["removed"]})

                plain_message = strip_tags(html_message)
                from_email = f'Removed from the Queue of {event_model.name}'
                to = user_model.email

                mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)

            elif status == 'C':
                subject = f"Completed the Queue of {event_model.name}"

                html_message = render_to_string('notification.html', {**data, "notification": notification["complete"]})

                plain_message = strip_tags(html_message)
                from_email = f'Completed Queue of {event_model.name}'
                to = user_model.email

                mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)

            return True

        except:
            return False

    def get(self, request):
        try:
            queue_id = request.GET["queue_id"]
            queue_model = get_queue_model(queue_id)

            serializer = QueueSerializer(
                queue_model,
                many=False
            )
            return Response(
                serializer.data,
                status=status.HTTP_200_OK
            )

        except:
            return Response(
                status=status.HTTP_404_NOT_FOUND
            )

    def post(self, request):
        queue_data = QueueSerializer(data = request.data)

        try:
            event = Event.objects.get(id = request.data["event_id"])

            if queue_data.is_valid() and (not event.is_private) and event.status != "D" and add_new_participant(event.id):
                queue_data.save()

                return Response({
                        "success": True
                    },
                    status=status.HTTP_201_CREATED,
                )

            return Response({
                        "success": False
                    },
                status=status.HTTP_406_NOT_ACCEPTABLE
            )
        except:
            return Response(
                queue_data.errors(),
                status=status.HTTP_400_BAD_REQUEST
            )

    def put(self, request):
        queue_id = request.GET["queue_id"]
        serializer = QueueSerializer(
            get_queue_model(queue_id),
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
        try:
            _event_id = request.GET["event_id"]
            _user_id = request.GET["user_id"]
            _status = request.GET["status"]
            _status = _status.upper()

            vqueue_model = Queue.objects.get(user_id = _user_id, event_id = _event_id)
            if vqueue_model.status == "W"  and (_status == 'L' or _status == 'R' or _status == 'C'):

                vqueue_model.status = _status
                vqueue_model.save()

                self.__mail(
                    _user_id,
                    _event_id,
                    _status
                )
                
                return Response(
                    status = status.HTTP_200_OK
                )

            else:
                return Response(
                    status=status.HTTP_400_BAD_REQUEST
                )

        except Exception as e:
            print(e)
            return Response(
                status = status.HTTP_400_BAD_REQUEST
            )

class JoinPrivateQueue(APIView):

    permission_classes = [IsAdminUser]

    def post(self, request):

        try:
            _security_key = request.POST["security_key"]
            event = Event.objects.get(security_key = _security_key)

            if event and event.status != "D" and add_new_participant(event.id):
                queue_data = QueueSerializer(
                    data = {
                        "user_id": request.data["user_id"],
                        "event_id": event.id
                    }
                )

                serializer = EventSerializer(
                    event,
                    many=False
                )
                if queue_data.is_valid():
                    queue_data.save()
                    
                    return Response(
                        serializer.data,
                        status = status.HTTP_200_OK
                    )

                return Response({
                    "success": False,
                    "error": "Security key invalid"
                    }, status = status.HTTP_404_NOT_FOUND
                )

            else:
                return Response({
                    "success": False
                    }, 
                    status = status.HTTP_400_BAD_REQUEST
                )
        except:
            return Response({
                "success": False
                }, status = status.HTTP_406_NOT_ACCEPTABLE
            )


class UserPosition(APIView):
    
    permission_classes = [IsAdminUser]

    def get(self, request):

        try:
            _user_id = request.GET['user_id']
            _event_id = request.GET["event_id"]

            queue = Queue.objects.get(user_id = _user_id, event_id = _event_id)

            current_position = Queue.objects.filter(id__lte=queue.id, status="W").count()

            return Response({
                "position": current_position,
                "success": True,
            }, status = status.HTTP_200_OK)

        except:
            return Response({
                "success": False
                },
                status = status.HTTP_400_BAD_REQUEST
            )

class WaitingUsers(APIView):

    permission_classes = [IsAdminUser]

    def get(self, request):
        try:
            _event_id = request.GET["event_id"]
            queue = Queue.objects.filter(event_id = _event_id, status = "W")
            serializer = QueueSerializer(
                queue, 
                many=True
            )

            response = []
            
            for q in queue:
                current_position = Queue.objects.filter(id__lte=q.id, status="W").count()

                response.append({
                    "queue_id": q.id,
                    "user_id": q.user_id.id,
                    "first_name": q.user_id.first_name,
                    "last_name": q.user_id.last_name,
                    "current_position":  current_position,
                    "user_email": q.user_id.email
                })

            return Response(
                response,
                status=status.HTTP_200_OK
            )

        except:
            return Response(
                status = status.HTTP_400_BAD_REQUEST
            )


class UserEventLogs(APIView):

    permission_classes = [IsAdminUser]

    def get(self, request):
        try:
            _user_id = request.GET["user_id"]
            try:
                _status = request.GET["status"]
            except:
                _status = None

            if _status in ("W", "L", "R", "C"):
                queues = Queue.objects.filter(user_id = _user_id, status = _status)
            else:
                queues = Queue.objects.filter(user_id = _user_id)

            if _status == "W":
                response = []

                for q in queues:
                    _user_id = q.user_id.id
                    _event_id = q.event_id.id
                    queue = Queue.objects.get(user_id = _user_id, event_id = _event_id)
                    current_position = Queue.objects.filter(id__lte=queue.id, status="W").count()

                    queue_data = {
                        **EventSerializer(q.event_id).data,
                        "position": current_position,
                    }
                    response.append(queue_data)

                return Response(
                    response,
                    status=status.HTTP_200_OK
                )

            else:
                serializer = QueueSerializer(
                    queues,
                    many = True
                )

            return Response(
                serializer.data,
                status = status.HTTP_200_OK
            )

        except:
            return Response(
                status = status.HTTP_400_BAD_REQUEST
            )
