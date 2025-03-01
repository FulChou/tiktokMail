package org.edu.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@Data
public class UserDTO implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String email;

}
