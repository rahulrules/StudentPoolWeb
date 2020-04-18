package StudentPool.Resources;

import StudentPool.Filter.UserAuthenticationFilter;
import StudentPool.Services.ConfirmationService;
import StudentPool.model.Bookings;
import StudentPool.model.Confirmations;
import StudentPool.model.Link;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

//@Path("/")
public class ConfirmResource {

    private static final String AUTHORIZATION="Authorization";

    ConfirmationService confirmServices = new ConfirmationService();

    @GET
    public List<Confirmations> getRideConfirms(@PathParam("rideId") String id){//same pathparam names?, @pathparam refers to the final pathname
        return confirmServices.getConfirmForRIdeID(Integer.valueOf(id));

    }

    @POST
    public Response confirmRide(Confirmations newconfirm, @PathParam("rideId") String id,
                                @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        //setmethod of Bookings object is not be used as NOT null Timestamp is to be invoked via constructor
        String user_id= new UserAuthenticationFilter().extractUsername(httpHeaders.getRequestHeaders().get(AUTHORIZATION));
        Confirmations tempconfirm= new Confirmations(user_id,Integer.valueOf(id),
                newconfirm.getInstructions());
        Integer createdID= confirmServices.insertConfirmation(tempconfirm);
        URI uri=null;
        if(createdID>0){
            uri=uriInfo.getBaseUriBuilder()
                    .path(RidesRequestedResource.class)
                    .path(id)
                    .path("Confirmations")
                    .path(String.valueOf(createdID))
                    .build();
            tempconfirm.setId(createdID);

        }
        return Response.created(uri) //passing the created URI as location header
                .entity(tempconfirm)// building the object to show in response body
                .build();
    }

    @Path("/{confirmationId}")
    @GET
    public Confirmations getRideBookingbyID(@PathParam("confirmationId") String id, @Context UriInfo uriInfo){
        Confirmations fetchedConfirmation= confirmServices.getConfirmbyID(Integer.valueOf(id));

        return fetchedConfirmation;
    }

    @Path("/{confirmationId}")
    @DELETE
    public Link deleteConfirm(@PathParam("confirmationId") String id, @Context UriInfo uriInfo){
        Integer result= confirmServices.deleteconfirmID(Integer.valueOf(id));

        return new Link(RidesRequestedResource.getURIForAllRR(uriInfo),"Rides_Requested");
    }

    @Path("/{confirmationId}")
    @PUT
    public void updateConfirm(@PathParam("confirmationId") String id,@PathParam("rideId") int rideid,
                              Confirmations modifyconfirm, @Context UriInfo uriInfo){
        Confirmations tempconfirm= new Confirmations(modifyconfirm.getRiderequested_id(),modifyconfirm.getInstructions());
        Integer result= confirmServices.updateConfirmationbyID(tempconfirm,id);
    }


}
