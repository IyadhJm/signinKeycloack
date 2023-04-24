package com.example.signin.repository;

import com.example.signin.httpRequest.LoginRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<LoginRequest, Integer> {

}

