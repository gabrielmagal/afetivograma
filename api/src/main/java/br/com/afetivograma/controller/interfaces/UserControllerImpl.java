package br.com.afetivograma.controller.interfaces;

import br.com.afetivograma.view.dto.UserDto;

public interface UserControllerImpl {
    void createUser(UserDto userDto);
    UserDto getUserByUsername(String username);
    UserDto getUserById(String id);
    void enabledUser(String username, Boolean enabled);
}
