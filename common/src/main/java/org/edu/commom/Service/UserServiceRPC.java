package org.edu.commom.Service;

import org.edu.commom.domain.UserDTO;

import java.util.List;

public interface UserServiceRPC {

    int saveUser(UserDTO user);

    void updateUser(UserDTO user);

    void deleteUser(UserDTO user);

    List<UserDTO> getUserList();
}
