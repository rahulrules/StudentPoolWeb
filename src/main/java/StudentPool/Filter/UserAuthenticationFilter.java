package StudentPool.Filter;


import StudentPool.Services.UsersServices;
import StudentPool.model.Users;
import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;


@Provider
public class UserAuthenticationFilter implements ContainerRequestFilter {
    private static final String AUTHORIZATION="Authorization";
    private static final String BASICAUTH_PREFIX="Basic ";//basic word with a space
//    private static final String[] secureresouces={"rides_offered","bookings"};
//    private static final List<String > secureresourcellist=new ArrayList<>(Arrays.asList(secureresouces));
    private static final  String BOOKING="rides_offered";


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if(requestContext.getUriInfo().getPath().contains(BOOKING)){
            List<String> authtokenlist=requestContext.getHeaders().get(AUTHORIZATION);

            if(extractUsername(authtokenlist)!=null){
                return;
            }
            Response unauthorizedstatus= Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("User cannot access the resource")
                    .build();

            requestContext.abortWith(unauthorizedstatus);
        }
    }

    public String extractUsername(List<String> authtokenlist){

        if(authtokenlist!=null && authtokenlist.size()>0){

            String authtoken= authtokenlist.get(0);
            authtoken=authtoken.replaceFirst(BASICAUTH_PREFIX,"");
            String decodedstring= Base64.decodeAsString(authtoken);
            StringTokenizer tokenizer= new StringTokenizer(decodedstring,":");
            Users authuser= new Users();
            authuser.setUser_id(tokenizer.nextToken().toLowerCase());
            authuser.setUser_password(tokenizer.nextToken());

            if(new UsersServices().IsResgisteredUser(authuser)>0){

                return authuser.getUser_password();
            }
        }
        return null;
    }
}
