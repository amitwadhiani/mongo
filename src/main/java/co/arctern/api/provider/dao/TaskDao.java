package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Task entity repository layer
 */
@Repository
@PreAuthorize("isAuthenticated()")
public interface TaskDao extends PagingAndSortingRepository<Task, Long> {

}
