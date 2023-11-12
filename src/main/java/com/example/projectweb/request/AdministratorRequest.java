package com.example.projectweb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorRequest {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirm;
    private String userName;
}
