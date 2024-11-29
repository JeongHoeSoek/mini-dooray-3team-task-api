package com.nhnacademy.minidooray3teamaccountapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MileStone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "milestone")
    private List<Task> tasks;

    public enum Status {
        START, PROGRESS, END
    }

    public void patch(MileStone mileStone) {
        // 전달 값에 name 속성의 값이 있다면 넣기
        if (mileStone.name != null) {
            this.setName(mileStone.getName());
        }

        // 전달 값에 status 속성의 값 있다면 넣기
        if (mileStone.status != null) {
            this.setStatus(mileStone.getStatus());
        }
    }
}
