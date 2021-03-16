from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .models import Queue
from .serializers import QueueSerializer
from User.models import UserDetail
from django.core.mail import send_mail
from QyuApplication.settings import EMAIL_HOST_USER



def get_queue_model(queue_id):
    return Queue.objects.get(id=queue_id)


class QueueDetailApi(APIView):

    permission_classes = [IsAdminUser]

    def __mail(self, user_id, status):
        user_model = UserDetail.objects.get(id=user_id)
        try:
            if status == 'L':
                # TODO: left mail template goes here
                pass
            elif status == 'R':
                # TODO: removed mail template goes here
                pass
            elif status == 'C':
                # TODO: complete mail template goes here
                pass
            return True
        except:
            return False

    def get(self, request):
        try:
            queue_id = request.GET["event_id"]
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

        if queue_data.is_valid():
            queue_data.save()

            return Response(
                status=status.HTTP_201_CREATED,
            )

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
            _event_id = request.GET["queue_id"]
            _user_id = request.GET["user_id"]
            _status = request.GET["status"]
            _status = _status.upper()

            vqueue_model = Queue.objects.get(user_id = _user_id, event_id = _event_id)

            if vqueue_model.status == "W"  and (_status == 'L' or _status == 'R' or _status == 'C'):
                vqueue_model.status = _status
                vqueue_model.save()
                try:
                    self.__mail(
                        _user_id,
                        _status
                    )
                except:
                    return Response(
                        status = status.HTTP_400_BAD_REQUEST
                    )

            else:
                return Response(
                    status=status.HTTP_400_BAD_REQUEST
                )

        except:
            return Response(
                status = status.HTTP_400_BAD_REQUEST
            )
