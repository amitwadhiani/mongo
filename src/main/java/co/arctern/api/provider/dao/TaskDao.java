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

    /**
     * fetch tasks filtered by type.
     * @param type
     * @param pageable
     * @return
     */
    Page<Task> findByType(OfferingType type, Pageable pageable);

    /**
     * fetch tasks filtered by type and within a time range.
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(OfferingType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks for a particular area.
     * @param areaIds
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdIn(List<Long> areaIds, Pageable pageable);

    /**
     * fetch tasks for a particular area within a time range.
     * @param areaIds
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch cancellation requested tasks.
     * @param pageable
     * @return
     */
    Page<Task> findByCancellationRequestedTrue(Pageable pageable);
}
