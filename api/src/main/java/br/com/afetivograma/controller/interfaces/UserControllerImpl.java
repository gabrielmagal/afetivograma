package br.com.afetivograma.controller.interfaces;

import br.com.afetivograma.view.dto.UserDto;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;
import java.util.UUID;

public interface UserControllerImpl {
    AccessTokenResponse generateToken(String username, String password);
    void createUser(UserDto userDto);
    List<RoleRepresentation> getRolesAllowed ();
    UserDto getUserByUsername(String username);
    UserDto getUserById(UUID id);
    void enabledUser(String username, Boolean enabled);
}
