package com.example.signin.repository;

import com.example.signin.httpRequest.CreateUserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CreateUserRequest, Integer> {

}
