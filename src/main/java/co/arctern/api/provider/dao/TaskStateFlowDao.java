package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskStateFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * TaskStateFlow entity repository layer
 */
@Repository
public interface TaskStateFlowDao extends PagingAndSortingRepository<TaskStateFlow, Long> {
}
