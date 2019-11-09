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
   public Users getUser(@PathParam("id") Integer id, @Context UriInfo uriInfo){
        Users fetcheduser= newuserservice.getUserFromID(id);
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

    @GET
    @Path("/login")
    public Users loginaccount(Users newuser,@Context UriInfo uriInfo){
        Integer id=newuserservice.IsResgisteredUser(newuser);
        if(id>0){
           return getUser(id,uriInfo);//change th log in links method
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

//    private String getURIforRidesOffered(UriInfo uriInfo){//shoud I pass the object or just its id
//        String uri=uriInfo.getBaseUriBuilder()
//                .path(RidesOfferedResource.class)
//                .build()
//                .toString();
//        return uri;
//    }
}
