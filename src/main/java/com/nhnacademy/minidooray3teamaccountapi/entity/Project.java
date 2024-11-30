package com.nhnacademy.minidooray3teamaccountapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<ProjectMember> members;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    public enum Status {
        ACTIVE, SLEEP, CLOSED
    }

}
