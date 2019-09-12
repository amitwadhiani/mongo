package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskReason;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * TaskReason entity repository layer.
 */
@Repository
public interface TaskReasonDao extends PagingAndSortingRepository<TaskReason, Long> {
}
