package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Task entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface TaskDao extends PagingAndSortingRepository<Task, Long> {
}
