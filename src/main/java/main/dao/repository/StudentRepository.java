package main.dao.repository;

import main.dao.entity.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    @Transactional
    void deleteAllByGroupAndCreatedAtBefore(@Param("group") String group, @Param("time") OffsetDateTime createdAt);
}
