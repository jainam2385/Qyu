from .serializers  import EventSerializer
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .models import Event
import uuid


def is_unique(key):
    try:
        Event.objects.get(security_key=key)
        return False
    except:
        return True


def generate_security_key():
    is_generated=False
    security_key = '0'*10

    while not is_generated:
        security_key = str(uuid.uuid4())
        security_key = security_key.upper()
        security_key = security_key.replace("-","")
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
            event_model = Event.objects.get(id = event_id)

            serializer = EventSerializer(
                event_model,
                many=False
            )

            return Response(
                serializer.data,
                status=status.HTTP_202_ACCEPTED
            )
        except:
            return  Response(
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
                status = status.HTTP_200_OK
            )

        except:
            return Response(
                status = status.HTTP_400_BAD_REQUEST
            )


class StartEvent(APIView):

    permission_classes = [IsAdminUser]

    def post(self, request):
        try:
            event_id = request.GET["event_id"]
            event_model = get_event_model(event_id)
            if event_model.status == "R":
                event_model.status = "A"
                event_model.save()

                # TODO MAIL ALL USERS REGARDING START OF THE EVENT

                return Response(
                    status=status.HTTP_200_OK
                )
            else:
                return Response(
                    status = status.HTTP_406_NOT_ACCEPTABLE
                )
        except:
            return Response(
                status = status.HTTP_400_BAD_REQUEST
            )

class EndEvent(APIView):

    permission_classes = [IsAdminUser]

    def post(self, request):
        try:
            event_id = request.GET["event_id"]
            event_model = get_event_model(event_id)
            if event_model.status == "A":
                event_model.status = "D"
                event_model.save()

            # TODO MAIL USERS OF EVENT ENDING

        except:
            return Response(
                status = status.HTTP_200_OK
            )
