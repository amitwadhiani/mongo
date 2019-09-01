package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskStateFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * TaskStateFlow entity repository layer
 */
@RepositoryRestResource(exported = false)
@PreAuthorize("isAuthenticated()")
public interface TaskStateFlowDao extends PagingAndSortingRepository<TaskStateFlow, Long> {
}
