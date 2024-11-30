package com.nhnacademy.minidooray3teamaccountapi.repository;
import com.nhnacademy.minidooray3teamaccountapi.dto.UserDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
