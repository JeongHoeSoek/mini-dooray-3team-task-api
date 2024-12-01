package com.nhnacademy.minidooray3teamaccountapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String name;

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
