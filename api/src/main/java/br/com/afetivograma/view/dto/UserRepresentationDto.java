package br.com.afetivograma.view.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserRepresentationDto extends UserRepresentation {
    public UserRepresentationDto(UserDto userDto) {
        setEmail(userDto.getEmail());
        setUsername(userDto.getUsername());
        setFirstName(userDto.getFirstName());
        setLastName(userDto.getLastName());
        setEnabled(userDto.getEnabled());

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("PSYCHOLOGIST", List.of(userDto.getPsychologist()));
        setAttributes(attributes);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(userDto.getCredentialsDto().get(0).getValue());
        credentialRepresentation.setTemporary(false);

        setCredentials(Collections.singletonList(credentialRepresentation));
    }

    public UserRepresentationDto(UserRepresentation userRepresentation) {
        setEmail(userRepresentation.getEmail());
        setUsername(userRepresentation.getUsername());
        setFirstName(userRepresentation.getFirstName());
        setLastName(userRepresentation.getLastName());
        setEnabled(userRepresentation.isEnabled());
    }
}
