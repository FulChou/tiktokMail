package org.edu.commom.Service;

import org.edu.commom.domain.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(String id);

    List<User> getUserList();

    User getUserById(String id);

    List<User> getUsersByUsername(String username);
}
