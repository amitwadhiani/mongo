package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Event entity repository layer
 */
@Repository
public interface EventDao extends PagingAndSortingRepository<Event,Long> {
}
