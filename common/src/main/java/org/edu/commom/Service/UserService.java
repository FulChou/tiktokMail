package org.edu.commom.Service;

import org.edu.commom.domain.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    List<User> getUserList();
}
