package main.dao.repository;

import main.dao.entity.Pass;
import org.springframework.data.repository.CrudRepository;

public interface PassRepository extends CrudRepository<Pass, Long> {
}
