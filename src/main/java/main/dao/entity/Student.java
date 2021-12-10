package main.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name", nullable = false)
    private String name;

    @EqualsAndHashCode.Include
    @Column(name = "student_group", nullable = false)
    private String group;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private List<Exam> exams;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private List<Pass> passes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
