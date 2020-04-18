package StudentPool.Resources;

import StudentPool.Services.UsersServices;
import StudentPool.model.Users;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    UsersServices newuserservice = new UsersServices();

    @GET
    public List<Users> getUserslist() {
        return newuserservice.getAllUsers();
    }

    @GET
   @Path("/{id}")
   public Users getUser(@PathParam("id") String id, @Context UriInfo uriInfo){
        Users fetcheduser= newuserservice.getUserFromID(Integer.valueOf(id));
        fetcheduser.addlink(getURIforSelf(uriInfo,fetcheduser),"self");
        fetcheduser.addlink(RidesOfferedResource.getURIForAllRO(uriInfo),"Rides Offered");
        return fetcheduser;

   }

    @POST
    @Path("/createaccount") //A sub resource can be created as a separate class which has only POST method
    public Response Createnewaccount(Users newuser, @Context UriInfo uriinfo){
        Users usertoadd= newuserservice.CreateAccount(newuser);
        System.out.println(usertoadd.getId());
        URI uri=null;
        if(usertoadd!=null){
             uri =uriinfo.getBaseUriBuilder().path(UsersResource.class)
                     .path(String.valueOf(usertoadd.getId())).build();
        }
        return Response.created(uri)
                .entity(usertoadd)
                .build();

    }

    @POST
    @Path("/login")
    public Users loginaccount(Users newuser,@Context UriInfo uriInfo){
        Integer id=newuserservice.IsResgisteredUser(newuser);
        if(id>0){

           return getUser(String.valueOf(id),uriInfo);//change the login links method
        }
        return null;
    }




    private String getURIforSelf(UriInfo uriInfo,Users user){//shoud I pass the object or just its id
        String uri=uriInfo.getBaseUriBuilder()
                .path(UsersResource.class)
                .path(String.valueOf(user.getId()))
                .build()
                .toString();
        return uri;
    }



}
