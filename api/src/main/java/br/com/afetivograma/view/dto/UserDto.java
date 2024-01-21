package br.com.afetivograma.view.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String profile;
    private String psychologist;
}
