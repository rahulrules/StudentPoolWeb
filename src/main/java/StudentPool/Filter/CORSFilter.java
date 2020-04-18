package StudentPool.Filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;


@Provider
public class CORSFilter implements ContainerResponseFilter {
    private static final String Origin="Origin";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
       List<String> requestorigin = requestContext.getHeaders().get(Origin);// This line is reduundant, All domain requests will have origin for request to be successful
       if(requestorigin!=null){
           responseContext.getHeaders().add("Access-Control-Allow-Origin","*");
           responseContext.getHeaders().add("Access-Control-Allow-Headers","Origin,Content-Type,Accept,Authorization");
           responseContext.getHeaders().add("Access-Control-Expose-Headers","Origin,Content-Type");//Multivalued map
           responseContext.getHeaders().add("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS,HEAD");
           responseContext.getHeaders().add("Access-Control-Max-Age","86400");
       }
       responseContext.getHeaders().add("X-Powered-By", "Rahulbrains");
    }
}
