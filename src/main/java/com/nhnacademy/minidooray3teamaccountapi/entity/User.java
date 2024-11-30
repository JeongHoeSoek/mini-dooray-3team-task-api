package com.nhnacademy.minidooray3teamaccountapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "users")
public class User {

    @Id
    private String id;

    private String username;

    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<ProjectMember> projectMembers;

}
