package com.nhnacademy.minidooray3teamaccountapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MileStone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneId;

    private String name;

    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "milestone")
    private List<Task> tasks;

    public enum Status {
        START, PROGRESS, END
    }
}
