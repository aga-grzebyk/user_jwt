package com.agagrzebyk.user_jwt.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username jest wymagany")
    private String username;

    private String firstName;
    private String lastName;

    @Email
    @NotBlank(message = "e-mail jest wymagany")
    private String email;

    @NotBlank(message = "hasło jest wymagane")
    private String password;

    private String indexNumber;

    private int active;

    private String roles = "";

    private String permissions = "";



    public User(String username, String email, String password, String roles, String permissions) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.active = 1;
    }



    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split("'"));
        }
        return new ArrayList<>();
    }

}
