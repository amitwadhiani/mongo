package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskEvent;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * TaskEvent entity repository layer
 */
@RepositoryRestResource(exported = false)
@PreAuthorize("isAuthenticated()")
public interface TaskEventDao extends PagingAndSortingRepository<TaskEvent, Long> {
}
