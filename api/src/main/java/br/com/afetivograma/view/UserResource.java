package br.com.afetivograma.view;

import br.com.afetivograma.controller.interfaces.UserControllerImpl;
import br.com.afetivograma.view.dto.UserDto;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;
import java.util.UUID;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserControllerImpl userControllerImpl;

    @POST
    @Path("/{username}/{password}")
    public AccessTokenResponse generateToken(@PathParam("username") String username, @PathParam("password") String password) {
        return userControllerImpl.generateToken(username, password);
    }

    @POST
    public void createUser(@RequestBody UserDto userDto) {
        userControllerImpl.createUser(userDto);
    }

    @GET
    @Path("/roles")
    public List<RoleRepresentation> getROlesAllowed() {
        return userControllerImpl.getRolesAllowed();
    }

    @POST
    @Path("enabled-user")
    @Authenticated
    public void enabledUser(@QueryParam("username") String username, @QueryParam("enabled") Boolean enabled) {
        userControllerImpl.enabledUser(username, enabled);
    }

    @GET
    @Path("/username/{username}")
    @Authenticated
    public UserDto getUserByUsername(@PathParam("username") String username) {
        return userControllerImpl.getUserByUsername(username);
    }

    @GET
    @Path("/{id}")
    @Authenticated
    public UserDto getUserById(@PathParam("id") UUID id) {
        return userControllerImpl.getUserById(id);
    }
}