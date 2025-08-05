package com.github.movierating.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import lombok.Setter;

@Entity(name = "USERS")
@Setter
@SequenceGenerator(name = "DEFAULT_GEN", sequenceName = "USER_SEQUENCE")
public class User extends CommonEntity {

    private String email;
    private String password;

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

}
