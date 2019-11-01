package StudentPool.Resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UsersResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getUserslist() {
        return "Processing user list";
    }
}
