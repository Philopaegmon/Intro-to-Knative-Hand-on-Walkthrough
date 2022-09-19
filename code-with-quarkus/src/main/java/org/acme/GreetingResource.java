package org.acme;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;

@Path("/greeting")
public class GreetingResource {

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello(@PathParam(value = "name") String name) {
        return Uni.createFrom()
            .item(MessageFormat.format("Hello {0} from RESTEasy Reactive", name));
    }
}
