package com.nhnacademy.minidooray3teamaccountapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private Long userId;

    private Role role;

    @OneToMany(mappedBy = "projectMember")
    private List<Task> tasks;

    @OneToMany(mappedBy = "projectMember")
    private List<Comment> comments;

    public enum Role {
        MEMBER, ADMIN
    }

}
