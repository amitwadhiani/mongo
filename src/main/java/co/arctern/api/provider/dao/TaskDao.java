package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Task entity repository layer
 */
@Repository
public interface TaskDao extends PagingAndSortingRepository<Task, Long> {

    Page<Task> findByType(OfferingType type, Pageable pageable);

    Page<Task> findByTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(OfferingType type, Timestamp start, Timestamp end, Pageable pageable);

    Page<Task> findByDestinationAddressAreaIdIn(List<Long> areaIds, Pageable pageable);

    Page<Task> findByDestinationAddressAreaIdInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable);

    Page<Task> findByCancellationRequestedTrue(Pageable pageable);
}
