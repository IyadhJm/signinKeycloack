package com.example.signin.httpRequest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CreateUserRequest {
    String username;
    String password;
    String email;
    String firstname;
    String lastname;
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
