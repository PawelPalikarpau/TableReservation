package my.project.repository;

import my.project.domain.TableModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableModelRepository extends CrudRepository<TableModel, Long> {
    List<TableModel> findAllByCapacity(Integer capacity);
}
