package com.nhnacademy.minidooray3teamaccountapi.repository;
import com.nhnacademy.minidooray3teamaccountapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
