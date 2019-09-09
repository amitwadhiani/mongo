package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskReason;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskReasonDao extends PagingAndSortingRepository<TaskReason, Long> {
}
