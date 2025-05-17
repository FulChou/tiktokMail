package org.edu.common.domain.DatoDTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDataDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Date created_at;
}