package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.TaskStateFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStateFlowDao extends PagingAndSortingRepository<TaskStateFlow, Long> {
}
