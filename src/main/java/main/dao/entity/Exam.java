package main.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mark", nullable = false)
    private String mark;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

}
