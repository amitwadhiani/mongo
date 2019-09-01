package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskStateFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * TaskStateFlow entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface TaskStateFlowDao extends PagingAndSortingRepository<TaskStateFlow, Long> {
}
