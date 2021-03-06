package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * TaskEvent entity repository layer
 */
@Repository
public interface TaskEventDao extends PagingAndSortingRepository<Event,Long> {
}
