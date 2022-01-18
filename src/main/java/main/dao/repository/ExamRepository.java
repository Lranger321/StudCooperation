package main.dao.repository;

import main.dao.entity.Exam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long> {

    List<Exam> findAllByName(String name);

    List<Exam> findBySemester(int semester);
}
