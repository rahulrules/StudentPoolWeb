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
import java.net.URI;
import java.util.List;

@Path("rides_offered")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RidesOfferedResource {

    RidesOfferedServices newros= new RidesOfferedServices();

    @GET
    public List<Rides> getridesoffered(@BeanParam RidesFilterBean filterBean) {
        //Correct this method to give proper query based answers- Have to change SQL query statement
        // returned list tobe  a new copy of the object rather than providing giving the original object

        if(filterBean.getToplace()!=null){
            return newros.RidesToCity(filterBean.getToplace());
        }
//        if(filterBean.getStart()>0 && filterBean.getSize()>0){
//            return newros.getAllOfferedRidesPaginated(filterBean.getStart(),filterBean.getSize());
//        }

        return newros.getAllOfferedRides();
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

    @Path("/{id}")
    @GET
    public Rides getRide(@PathParam("id") String id, @Context UriInfo uriInfo){
        Rides fetchedride=newros.getRideFromId(Integer.valueOf(id));
        fetchedride.addlink(getURIForAllRO(uriInfo),"RidesOffered");

        return fetchedride;
    }

    @Path("/{id}")
    @DELETE
    public void deleteRide(@PathParam("id") String id, @Context UriInfo uriInfo){
        Integer result= newros.deleteRidebyID(Integer.valueOf(id));

    }

   // this redirection of resources we want to hapepnd for all HTTP methods.

    @Path("/{id}/bookings")
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

    //Write PUT & Delete Method to have PUT & Delete Authorization required; Only the one who has posted can

}
