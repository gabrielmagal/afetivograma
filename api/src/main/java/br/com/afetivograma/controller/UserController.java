package br.com.afetivograma.controller;

import br.com.afetivograma.view.dto.CredentialsDto;
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
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    public AccessTokenResponse generateToken(String username, String password) {
        Keycloak keycloakUser = KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(keycloakRealm)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .username(username)
                .password(password)
                .grantType("password")
                .build();

        AccessTokenResponse accessTokenResponse = keycloakUser.tokenManager().getAccessToken();
        keycloakUser.close();
        return accessTokenResponse;
    }

    public void createUser(UserDto userDto) {
        List<RoleRepresentation> roleRepresentationList = getRolesAllowed();
        AtomicReference<RoleRepresentation> roleRepresentation = new AtomicReference<>(new RoleRepresentation());

        roleRepresentationList.forEach(role -> {
            if (role.getName().equals(userDto.getProfile()))
                roleRepresentation.set(role);
        });

        if (Objects.isNull(roleRepresentation.get().getId())) {
            throw new RuntimeException("A role selecionada não existe.");
        }

        if (roleRepresentation.get().getName().equals("USER")) {
            List<UserRepresentation> userRepresentationList = keycloak.realm(keycloakRealm).users().search(userDto.getPsychologist());
            if(userRepresentationList.isEmpty()) {
                throw new RuntimeException("O psicólogo selecionado não existe.");
            }
            if(keycloak.realms().realm(keycloakRealm).users().get(userRepresentationList.get(0).getId()).roles().getAll()
                    .getRealmMappings().stream().noneMatch(role -> role.getName().equals("PSYCHOLOGIST"))) {
                throw new RuntimeException("O usuário selecionado como psicólogo não tem esse perfil.");
            }
        }

        UserRepresentationDto userRepresentationDto = new UserRepresentationDto(userDto);
        userRepresentationDto.setClientRoles(Map.of(keycloakClientId, List.of(userDto.getProfile())));

        keycloak.realms().realm(keycloakRealm).users().create(userRepresentationDto).close();

        UserRepresentation createdUser = keycloak.realm(keycloakRealm).users().search(userDto.getUsername()).get(0);

        keycloak.realm(keycloakRealm)
                .users()
                .get(createdUser.getId())
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation.get()));
    }

    public UserDto getUserByUsername(String username) {
        UsersResource usersResource = keycloak.realms().realm(keycloakRealm).users();
        if(usersResource.count() == 0)
            return new UserDto();
        List<UserRepresentation> userRepresentationList = usersResource.searchByUsername(username, true);
        if(userRepresentationList.isEmpty())
            return new UserDto();
        return convertUserRepresentationToUserDto(userRepresentationList.get(0));
    }

    public UserDto getUserById(UUID id) {
        UsersResource usersResource = keycloak.realms().realm(keycloakRealm).users();
        if(usersResource.count() == 0)
            return new UserDto();
        UserResource userResource = usersResource.get(id.toString());
        if(Objects.isNull(userResource))
            return new UserDto();
        return convertUserRepresentationToUserDto(userResource.toRepresentation());
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

    public List<RoleRepresentation> getRolesAllowed () {
        List<RoleRepresentation> roleRepresentationList = keycloak.realms().realm(keycloakRealm).roles().list();
        roleRepresentationList.removeIf(roleRepresentation ->
                roleRepresentation.getName().equals("default-roles-afetivograma") ||
                        roleRepresentation.getName().equals("offline_access") ||
                        roleRepresentation.getName().equals("uma_authorization") ||
                        roleRepresentation.getName().equals("ADMINISTRADOR")
        );
        return roleRepresentationList;
    }

    public List<RoleRepresentation> getRolesUserId(String id) {
        List<RoleRepresentation> roleRepresentationList = keycloak.realms().realm(keycloakRealm).users().get(id).roles().getAll().getRealmMappings();
        roleRepresentationList.removeIf(roleRepresentation ->
                roleRepresentation.getName().equals("default-roles-afetivograma") ||
                        roleRepresentation.getName().equals("offline_access") ||
                        roleRepresentation.getName().equals("uma_authorization"));

        return roleRepresentationList;
    }

    public UserDto convertUserRepresentationToUserDto(UserRepresentation userRepresentation) {
        UserDto userDto = new UserDto();
        userDto.setId(userRepresentation.getId());
        userDto.setEmail(userRepresentation.getEmail());
        userDto.setUsername(userRepresentation.getUsername());
        userDto.setFirstName(userRepresentation.getFirstName());
        userDto.setLastName(userRepresentation.getLastName());
        userDto.setEnabled(userRepresentation.isEnabled());

        userDto.setProfile(getRolesUserId(userRepresentation.getId()).get(0).getName());

        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if(!attributes.isEmpty() && attributes.containsKey("PSYCHOLOGIST")) {
            userDto.setPsychologist(attributes.get("PSYCHOLOGIST").get(0));
        }

        CredentialsDto credentials = new CredentialsDto();
        List<CredentialRepresentation> credentialRepresentationList = userRepresentation.getCredentials();
        if(Objects.nonNull(credentialRepresentationList)) {
            credentials.setValue(credentialRepresentationList.get(0).getValue());
        } else {
            credentials.setValue("************");
        }
        userDto.setCredentialsDto(Collections.singletonList(credentials));
        return userDto;
    }
}
