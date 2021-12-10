package main.dao.repository;

import main.dao.entity.Exam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long> {
}
