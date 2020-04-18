package StudentPool.Resources;


import StudentPool.Resources.Beans.RidesFilterBean;
import StudentPool.Services.RidesOfferedServices;
import StudentPool.Services.RidesRequestServices;
import StudentPool.model.Rides;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("rides_requested")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RidesRequestedResource {

   RidesRequestServices newrrs = new RidesRequestServices();

    @GET
    public List<Rides> getridesrequested(@BeanParam RidesFilterBean filterBean, @Context UriInfo uriInfo) {
        //Correct this method to give proper query based answers- Have to change SQL query statement
        // returned list tobe  a new copy of the object rather than providing giving the original object
        if(filterBean.getToplace()!=null){
            List<Rides> result= newrrs.getAllRequestedRidesPaginated(newrrs.RidesToCity(filterBean.getToplace()),filterBean.getStart(),filterBean.getSize());
            for(Rides ride: result){
                ride.addlink(getURIForAllRR(uriInfo)+"/"+ride.getId(),"self");
            }
            return result;
        }
        if(filterBean.getStart()!=null&&filterBean.getSize()!=null){
            if(filterBean.getStart()>0 && filterBean.getSize()>0){
                List<Rides> result= newrrs.getAllRequestedRidesPaginated(newrrs.getAllRequestedRides(),filterBean.getStart(),filterBean.getSize());
                for(Rides ride: result){
                    ride.addlink(getURIForAllRR(uriInfo)+"/"+ride.getId(),"self");
                }
                return result;
            }
//            List<Rides> result=newros.getAllOfferedRidesPaginated(newros.getAllOfferedRides(),0,5);
        }

        List<Rides> result= newrrs.getAllRequestedRidesPaginated(newrrs.getAllRequestedRides(),0,5);
        for(Rides ride: result){
            ride.addlink(getURIForAllRR(uriInfo)+"/"+ride.getId(),"self");
        }
        return result;

    }

    @POST//correct this method to suit JSON input for date and time
    public Response RequestRide(Rides newride, @Context UriInfo uriInfo){
        Integer createdid= newrrs.InsertIntoRR(newride);
        URI uri=null;
        if(createdid>0){
            uri =uriInfo.getBaseUriBuilder().path(RidesRequestedResource.class)
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
        Rides fetchedride= newrrs.getRideFromId(Integer.valueOf(id));
        fetchedride.addlink(getURIForAllRR(uriInfo),"RidesRequested");
        fetchedride.addlink(getURIForAllRideConfirm(uriInfo),"RideConfirmations");
        fetchedride.addlink(getURIForAllRide(uriInfo,id),"test booking link");

        return fetchedride;
    }
    //Write PUT & Delete Method to have PUT & Delete Authorization required; Only the one who has posted can
    @Path("/{rideId}")
    @DELETE
    public void deleteRide(@PathParam("rideId") String id, @Context UriInfo uriInfo){
        Integer result= newrrs.deleteRidebyID(Integer.valueOf(id));

    }

    @Path("/{rideId}")
    @PUT
    public void updateRide(@PathParam("rideId") String id, Rides modifyride,@Context UriInfo uriInfo){
        Integer result= newrrs.updateRideInfo(modifyride,id);

    }

    // this redirection of resources we want to happen for all HTTP methods.

    @Path("/{rideId}/confirmations")
    public ConfirmResource getconfirmations(){
        return  new ConfirmResource();
    }


    public static String getURIForAllRR(UriInfo uriInfo){
        String uri=uriInfo.getBaseUriBuilder()
                .path(RidesRequestedResource.class)
                .build()
                .toString();

        return uri;
    }

    public static String getURIForAllRideConfirm(UriInfo uriInfo){
        String uri=uriInfo.getAbsolutePathBuilder()
                .path("confirmations")
                .toString();
        return uri;
    }

    public static String getURIForAllRide(UriInfo uriInfo, String id) {
        String uri = uriInfo.getBaseUriBuilder()
                .path(RidesRequestedResource.class)
                .path(RidesRequestedResource.class, "getconfirmations")
                .build(id)
                .toString();
        return uri;

    }





}
