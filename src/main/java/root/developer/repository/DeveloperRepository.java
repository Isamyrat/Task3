package root.developer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import root.developer.model.Developer;

@Repository
public interface DeveloperRepository extends CrudRepository<Developer, Long> {
    Developer findByEmail(String email);

    Boolean existsByEmail(String email);

    boolean existsById(Long id);
}
