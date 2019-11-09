package StudentPool.Resources;


import StudentPool.Filter.UserAuthenticationFilter;
import StudentPool.Services.BookingServices;
import StudentPool.model.Bookings;
import StudentPool.model.Link;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;


@Path("/")
public class BookingResource {

    private static final String AUTHORIZATION="Authorization";

    BookingServices bookingServices= new BookingServices();

    @GET
    public List<Bookings> getRideBookings(@PathParam("id") String id){//same pathparam names?, @pathparam refers to the final pathname
         return bookingServices.getBookingForRIdeID(Integer.valueOf(id));

    }

    @POST
    public Response bookRide(Bookings newbooking, @PathParam("id") String id,
                             @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        //setmethod of Bookings object is not be used as NOT null Timestamp is to be invoked via constructor
//        String user_id= new UserAuthenticationFilter().extractUsername(httpHeaders.getRequestHeaders().get(AUTHORIZATION));
        Bookings tempbooking= new Bookings("rxj0007@ucmo.edu",Integer.valueOf(id),
                newbooking.getSlots_booked(),newbooking.getInstructions());
        Integer createdID=bookingServices.insertBooking(tempbooking);
        URI uri=null;
        if(createdID>0){
            uri=uriInfo.getBaseUriBuilder()
                    .path(RidesOfferedResource.class)
                    .path(id)
                    .path("bookings")
                    .path(String.valueOf(createdID))
                    .build();
            tempbooking.setId(createdID);

        }
        return Response.created(uri) //passing the created URI as location header
                .entity(tempbooking)// building the object to show in response body
                .build();
    }

    @Path("/{id}")
    @DELETE
    public Link deleteBooking(@PathParam("id") String id, @Context UriInfo uriInfo){
        Integer result= bookingServices.deletebookID(Integer.valueOf(id));

        return new Link(RidesOfferedResource.getURIForAllRO(uriInfo),"Rides_Offered");
    }


}
