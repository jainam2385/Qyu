from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAdminUser
from .serializers import OrganizationReviewSerializer
from organization.serializers import OrganizationDetailSerailizer
from .models import ReviewOrganization
from vqueue.models import Queue
from organization.models import OrganizationDetail
from event.models import Event



class ReviewDetailApi(APIView):
    
    permission_classes = [IsAdminUser]

    def __change_organization_rating(self, _organization_id):
        organization = OrganizationDetail.objects.get(id = _organization_id)

        organization_review = ReviewOrganization.objects.filter(organization_id = _organization_id)
        total_reviews = organization_review.count()

        if total_reviews >= 1:
            sum_reviews = sum([curr_review.rating for curr_review in organization_review])

            average = round(sum_reviews / total_reviews, 1)
            organization.rating = average
            organization.save()

    def get(self, request):
        try:
            _user_id = request.GET["user_id"]

            model = ReviewOrganization.objects.filter(user_id = _user_id)
            serializer = OrganizationReviewSerializer(
                model,
                many = True,
            )

            return Response(
                serializer.data, 
                status = status.HTTP_200_OK
            )

        except:
            return Response({
                "success": False
            }, status = status.HTTP_400_BAD_REQUEST)

    def post(self, request):
        review_data = OrganizationReviewSerializer(data = request.data)

        if review_data.is_valid():
            if 0.0 <= review_data.validated_data["rating"] <= 10.0:
                review_data.save()

                _organization_id = request.POST["organization_id"]
                self.__change_organization_rating(_organization_id)

                return Response({
                    "success": True
                },status = status.HTTP_201_CREATED)

            else:
                return Response({
                    "success": False,
                    "error": "Rating should not be greater than 10 or less than 0"
                }, status=status.HTTP_406_NOT_ACCEPTABLE)
        else:
            return Response(
                review_data.errors,
                status = status.HTTP_400_BAD_REQUEST
            )
    
    def delete(self, request):
        try:
            _user_id = request.GET["user_id"]
            _organization_id = request.GET["organization_id"]

            review_model = ReviewOrganization.objects.get(
                user_id = _user_id,
                organization_id = _organization_id
            )

            review_model.delete()

            _organization_id = request.POST["organization_id"]
            self.__change_organization_rating(_organization_id)

            return Response({
                "success": True
            }, status = status.HTTP_204_NO_CONTENT)

        except:
            return Response({
                "success": False
            })


class ListUserOrganizations(APIView):
    
    permission_classes = [IsAdminUser]

    def get(self, request):
        try:
            _user_id = request.GET["user_id"]
            _status = None

            if "status" in request.GET:
                _status = request.GET["status"]
            
            response = {}
            if _status == "reviewleft":
                for i in Queue.objects.all():
                    try:
                        ReviewOrganization.objects.get(
                            user_id=_user_id, 
                            organization_id=i.event_id.organization_id.id
                        )
                    except:
                        response[i.event_id.organization_id.id] = OrganizationDetailSerailizer(
                            i.event_id.organization_id
                        ).data

            if _status == "reviewdone":
                for i in Queue.objects.all():
                    try:
                        ReviewOrganization.objects.get(
                            user_id=_user_id, 
                            organization_id=i.event_id.organization_id.id
                        )   
                        response[i.event_id.organization_id.id] = OrganizationDetailSerailizer(
                            i.event_id.organization_id
                        ).data
                    except:
                        pass

            return Response(
                response.values(),
                status = status.HTTP_200_OK
            )
        except:
            return Response({
                "success": False
            }, status = status.HTTP_400_BAD_REQUEST)


class ReviewList(APIView):

    permission_classes=[IsAdminUser]

    def get(self, request):
        try:
            _organization_id = request.GET["organization_id"]

            reviews = ReviewOrganization.objects.filter(organization_id = _organization_id)
            
            serializer = OrganizationReviewSerializer(
                reviews,
                many = True
            )

            return Response(
                serializer.data, 
                status = status.HTTP_200_OK
            )

        except:
            return Response({
                "success": False
                }, status = status.HTTP_400_BAD_REQUEST
            )
