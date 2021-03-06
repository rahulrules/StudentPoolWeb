package StudentPool.Resources;

import StudentPool.Resources.Beans.RidesFilterBean;
import StudentPool.Services.RidesOfferedServices;
import StudentPool.model.Bookings;
import StudentPool.model.Rides;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

@Path("rides_offered")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RidesOfferedResource {

   private RidesOfferedServices newros= new RidesOfferedServices();

    @GET
    public List<Rides> getridesoffered(@BeanParam RidesFilterBean filterBean, @Context UriInfo uriInfo) {
        //Correct this method to give proper query based answers- Have to change SQL query statement
        // returned list tobe  a new copy of the object rather than providing giving the original object
        if(filterBean.getToplace()!=null){
            List<Rides> result=newros.getAllOfferedRidesPaginated(newros.RidesToCity(filterBean.getToplace()),filterBean.getStart(),filterBean.getSize());
            for(Rides ride: result){
                ride.addlink(getURIForAllRO(uriInfo)+"/"+ride.getId(),"self");
            }
            return result;
        }
        if(filterBean.getStart()!=null&&filterBean.getSize()!=null){
            if(filterBean.getStart()>0 && filterBean.getSize()>0){
                List<Rides> result= newros.getAllOfferedRidesPaginated(newros.getAllOfferedRides(),filterBean.getStart(),filterBean.getSize());
                for(Rides ride: result){
                    ride.addlink(getURIForAllRO(uriInfo)+"/"+ride.getId(),"self");
                }
                return result;
        }
//            List<Rides> result=newros.getAllOfferedRidesPaginated(newros.getAllOfferedRides(),0,5);
        }

        List<Rides> result= newros.getAllOfferedRidesPaginated(newros.getAllOfferedRides(),0,5);
        for(Rides ride: result){
            ride.addlink(getURIForAllRO(uriInfo)+"/"+ride.getId(),"self");
        }
        return result;

    }

    @POST//correct this method to suit JSON input for date and time
    public Response OfferRide(Rides newride,@Context UriInfo uriInfo){
        Integer createdid=newros.InsertIntoRO(newride);
        URI uri=null;
        if(createdid>0){
            uri =uriInfo.getBaseUriBuilder().path(RidesOfferedResource.class)
                    .path(String.valueOf(createdid)).build();
            newride.setId(createdid);
        }
        return Response.created(uri)
                .entity(newride)
                .build();

    }

    @Path("/{rideId}")
    @GET
    public Rides getRide(@PathParam("rideId") String id, @Context UriInfo uriInfo){
        Rides fetchedride=newros.getRideFromId(Integer.valueOf(id));
        fetchedride.addlink(getURIForAllRO(uriInfo),"RidesOffered");
        fetchedride.addlink(getURIForAllRideBookings(uriInfo),"RideBookings");
        fetchedride.addlink(getURIForAllRide(uriInfo,id),"test booking link");

        return fetchedride;
    }
    //Write PUT & Delete Method to have PUT & Delete Authorization required; Only the one who has posted can
    @Path("/{rideId}")
    @DELETE
    public void deleteRide(@PathParam("rideId") String id, @Context UriInfo uriInfo){
        Integer result= newros.deleteRidebyID(Integer.valueOf(id));

    }

    @Path("/{rideId}")
    @PUT
    public void updateRide(@PathParam("rideId") String id, Rides modifyride,@Context UriInfo uriInfo){
        Integer result= newros.updateRideInfo(modifyride,id);

    }

   // this redirection of resources we want to happen for all HTTP methods.

    @Path("/{rideId}/bookings")
    public BookingResource getbookings(){
        return  new BookingResource();
    }


    public static String getURIForAllRO(UriInfo uriInfo){
        String uri=uriInfo.getBaseUriBuilder()
                .path(RidesOfferedResource.class)
                .build()
                .toString();
        return uri;
    }

    public static String getURIForAllRideBookings(UriInfo uriInfo){
        String uri=uriInfo.getAbsolutePathBuilder()
                .path("bookings")
                .toString();
        return uri;
    }

    public static String getURIForAllRide(UriInfo uriInfo, String id) {
        String uri = uriInfo.getBaseUriBuilder()
                .path(RidesOfferedResource.class)
                .path(RidesOfferedResource.class, "getbookings")
                .build(id)
                .toString();
        return uri;

    }


}
