package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
     *
     * @param type
     * @param pageable
     * @return
     */
    Page<Task> findByType(OfferingType type, Pageable pageable);

    /**
     * fetch tasks filtered by type and within a time range.
     *
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(OfferingType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks for a particular area.
     *
     * @param areaIds
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdIn(List<Long> areaIds, Pageable pageable);

    /**
     * fetch tasks for a particular area within a time range.
     *
     * @param areaIds
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch cancellation requested tasks.
     *
     * @param pageable
     * @return
     */
    Page<Task> findByCancellationRequestedTrue(Pageable pageable);


    Page<Task> findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.destinationAddress.area.id IN (:areaIds) " +
            "AND (task.patientName = (:value) OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.state IN (:states) " +
            "AND task.createdAt >= (:start) " +
            "AND task.createdAt < (:end) " +
            "ORDER BY task.createdAt DESC")
    public Page<Task> filterByAreaIdsAndPatientDetails(@Param("areaIds") List<Long> areaIds, @Param("states") TaskState[] states, @Param("type") TaskType type, @Param("value") String value, @Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.state IN (:states) " +
            "AND (task.patientName = (:value) OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.createdAt >= (:start) " +
            "AND task.createdAt < (:end) " +
            "ORDER BY task.createdAt DESC")
    public Page<Task> filterByPatientDetails(@Param("states") TaskState[] states, @Param("value") String value, @Param("type") TaskType type, @Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

}
