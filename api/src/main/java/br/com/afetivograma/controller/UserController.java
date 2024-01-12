package br.com.afetivograma.controller;

import br.com.afetivograma.view.dto.UserDto;
import br.com.afetivograma.view.dto.UserRepresentationDto;
import br.com.afetivograma.controller.interfaces.UserControllerImpl;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class UserController implements UserControllerImpl {
    @ConfigProperty(name = "keycloak-url")
    String keycloakUrl;

    @ConfigProperty(name = "keycloak-realm")
    String keycloakRealm;

    @ConfigProperty(name = "keycloak-client-id")
    String keycloakClientId;

    @ConfigProperty(name = "keycloak-client-secret")
    String keycloakClientSecret;

    private static Keycloak keycloak;

    @PostConstruct
    public void initKeycloak() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(keycloakRealm)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .grantType("client_credentials")
                .build();
    }

    public void createUser(UserDto userDto) {
        UserRepresentationDto userRepresentationDto = new UserRepresentationDto(userDto);
        keycloak.realms().realm(keycloakRealm).users().create(userRepresentationDto).close();
    }

    public UserDto getUserByUsername(String username) {
        UsersResource usersResource = keycloak.realms().realm(keycloakRealm).users();
        if(usersResource.count() == 0)
            return new UserDto();
        List<UserRepresentation> userRepresentationList = usersResource.searchByUsername(username, true);
        if(userRepresentationList.isEmpty())
            return new UserDto();
        return new UserDto(userRepresentationList.get(0));
    }

    public UserDto getUserById(String id) {
        UsersResource usersResource = keycloak.realms().realm(keycloakRealm).users();
        if(usersResource.count() == 0)
            return new UserDto();
        UserResource userResource = usersResource.get(id);
        if(Objects.isNull(userResource))
            return new UserDto();
        return new UserDto(userResource.toRepresentation());
    }

    public void enabledUser(String username, Boolean enabled) {
        UsersResource usersResource = keycloak.realms().realm(keycloakRealm).users();
        List<UserRepresentation> userRepresentationList = usersResource.searchByUsername(username, true);
        if(!userRepresentationList.isEmpty()) {
            UserRepresentation userRepresentation = userRepresentationList.get(0);
            userRepresentation.setEnabled(enabled);
            usersResource.get(userRepresentation.getId()).update(new UserRepresentationDto(userRepresentation));
        }
    }
}
