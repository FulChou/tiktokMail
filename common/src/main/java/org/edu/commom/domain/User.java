package org.edu.commom.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@Data
public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private Integer age;

    public User(String id, String username, String password, Integer age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
    }
}
