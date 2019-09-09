package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskEventFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * TaskEventFlow entity repository layer
 */
@Repository
public interface TaskEventFlowDao extends PagingAndSortingRepository<TaskEventFlow, Long> {
}
