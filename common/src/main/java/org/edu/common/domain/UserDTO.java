package org.edu.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@Data
public class UserDTO implements Serializable {

    private String id;
    private String username;
    private String password;
    private String email;

    public UserDTO(String id, String username, String password, Integer age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
