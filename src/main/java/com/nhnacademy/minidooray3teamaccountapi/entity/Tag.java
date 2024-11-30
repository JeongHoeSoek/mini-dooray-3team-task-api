package com.nhnacademy.minidooray3teamaccountapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.REMOVE)
    private List<TaskTag> taskTags;

}
