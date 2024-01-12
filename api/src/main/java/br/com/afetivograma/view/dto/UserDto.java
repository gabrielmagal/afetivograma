package br.com.afetivograma.view.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private List<CredentialsDto> credentialsDto;

    public UserDto(UserRepresentation userRepresentation) {
        setId(userRepresentation.getId());
        setEmail(userRepresentation.getEmail());
        setUsername(userRepresentation.getUsername());
        setFirstName(userRepresentation.getFirstName());
        setLastName(userRepresentation.getLastName());
        setEnabled(userRepresentation.isEnabled());
        CredentialsDto credentials = new CredentialsDto();
        credentials.setValue(userRepresentation.getCredentials().get(0).getValue());
        setCredentialsDto(Collections.singletonList(credentials));
    }
}
