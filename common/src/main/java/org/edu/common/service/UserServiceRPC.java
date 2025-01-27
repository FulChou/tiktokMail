package org.edu.common.service;

import org.edu.common.domain.UserDTO;

import java.util.List;

public interface UserServiceRPC {

    int saveUser(UserDTO user);

    void updateUser(UserDTO user);

    void deleteUser(UserDTO user);

    List<UserDTO> getUserList();
}
