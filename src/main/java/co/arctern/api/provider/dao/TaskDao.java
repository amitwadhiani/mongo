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


    /**
     * fetch active tasks filtered by state and type.
     *
     * @param states
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks filtered by states and type.
     *
     * @param states
     * @param type
     * @param pageable
     * @return
     */
    Page<Task> findByIsActiveTrueAndStateInAndTypeOrderByCreatedAtDesc(
            TaskState[] states, TaskType type, Pageable pageable);

    /**
     * filter tasks through areas and patient details in a datetime range.
     *
     * @param areaIds
     * @param states
     * @param type
     * @param value
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.destinationAddress.area.id IN (:areaIds) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.state IN (:states) " +
            "AND task.createdAt >= (:start) " +
            "AND task.createdAt < (:end) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByAreaIdsAndPatientDetailsWithTime(@Param("areaIds") List<Long> areaIds, @Param("states") TaskState[] states, @Param("type") TaskType type, @Param("value") String value, @Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    /**
     * fetch filtered tasks by areaIds, states and type.
     *
     * @param areaIds
     * @param states
     * @param type
     * @param value
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.destinationAddress.area.id IN (:areaIds) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.state IN (:states) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByAreaIdsAndPatientDetails(@Param("areaIds") List<Long> areaIds, @Param("states") TaskState[] states, @Param("type") TaskType type, @Param("value") String value, Pageable pageable);

    /**
     * filter tasks through areas, patient details and refId(orderId).
     *
     * @param areaIds
     * @param refId
     * @param states
     * @param type
     * @param value
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.destinationAddress.area.id IN (:areaIds) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.refId = (:refId) " +
            "AND task.state IN (:states) " +
            "AND task.createdAt >= (:start) " +
            "AND task.createdAt < (:end) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByAreaIdsAndPatientDetailsWithRefIdWithTime(@Param("areaIds") List<Long> areaIds, @Param("refId") Long refId, @Param("states") TaskState[] states, @Param("type") TaskType type, @Param("value") String value, @Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    /**
     * fetch filtered tasks by areaIds, orderId and patient details.
     *
     * @param areaIds
     * @param refId
     * @param states
     * @param type
     * @param value
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.destinationAddress.area.id IN (:areaIds) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.refId = (:refId) " +
            "AND task.state IN (:states) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByAreaIdsAndPatientDetailsWithRefId(@Param("areaIds") List<Long> areaIds, @Param("refId") Long refId, @Param("states") TaskState[] states, @Param("type") TaskType type, @Param("value") String value, Pageable pageable);

    /**
     * filter tasks by patient details.
     *
     * @param states
     * @param value
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.state IN (:states) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.createdAt >= (:start) " +
            "AND task.createdAt < (:end) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByPatientDetailsAndTime(@Param("states") TaskState[] states, @Param("value") String value, @Param("type") TaskType type, @Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    /**
     * filter tasks by patientDetails.
     *
     * @param states
     * @param value
     * @param type
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.state IN (:states) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByPatientDetails(@Param("states") TaskState[] states, @Param("value") String value, @Param("type") TaskType type, Pageable pageable);

    /**
     * filter tasks by patient details and refId(orderId).
     *
     * @param states
     * @param refId
     * @param value
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.state IN (:states) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.refId = (:refId) " +
            "AND task.createdAt >= (:start) " +
            "AND task.createdAt < (:end) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByPatientDetailsWithRefIdWithTime(@Param("states") TaskState[] states, @Param("refId") Long refId, @Param("value") String value, @Param("type") TaskType type, @Param("start") Timestamp start, @Param("end") Timestamp end, Pageable pageable);

    /**
     * fetch filtered tasks by patientDetails, orderId.
     *
     * @param states
     * @param refId
     * @param value
     * @param type
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.isActive = 1" +
            "AND task.state IN (:states) " +
            "AND (task.patientName LIKE CONCAT('%',:value,'%') OR task.patientPhone = (:value) OR task.patientId = (:value)) " +
            "AND task.type = (:type) " +
            "AND task.refId = (:refId) " +
            "ORDER BY task.createdAt DESC ")
    public Page<Task> filterByPatientDetailsWithRefId(@Param("states") TaskState[] states, @Param("refId") Long refId, @Param("value") String value, @Param("type") TaskType type, Pageable pageable);

    /**
     * filter tasks by areas, states, type.
     *
     * @param areaIds
     * @param states
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, TaskType type, TaskState[] states, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * filter tasks by areaIds and state.
     *
     * @param areaIds
     * @param states
     * @param type
     * @param pageable
     * @return
     */
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInOrderByCreatedAtDesc(
            List<Long> areaIds, TaskState[] states, TaskType type, Pageable pageable);

    /**
     * filter tasks by areas, states, refId(orderId) and type.
     *
     * @param areaIds
     * @param states
     * @param refId
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, TaskState[] states, Long refId, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * filter tasks by areaIds, orderId, states and type.
     *
     * @param areaIds
     * @param states
     * @param refId
     * @param type
     * @param pageable
     * @return
     */
    Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeOrderByCreatedAtDesc(
            List<Long> areaIds, TaskState[] states, Long refId, TaskType type, Pageable pageable);

    /**
     * filter tasks by states, refId(orderId) and type.
     *
     * @param states
     * @param orderId
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<Task> findByIsActiveTrueAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, Long orderId, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks filtered by states, orderId and type.
     *
     * @param states
     * @param orderId
     * @param type
     * @param pageable
     * @return
     */
    public Page<Task> findByIsActiveTrueAndStateInAndRefIdAndTypeOrderByCreatedAtDesc(
            TaskState[] states, Long orderId, TaskType type, Pageable pageable);

    /**
     * fetch filtered tasks by state and less than a datetime (for cronjon ).
     *
     * @param start
     * @param state
     * @return
     */
    public List<Task> findByIsActiveTrueAndCreatedAtLessThanEqualAndState(Timestamp start, TaskState state);

    /**
     * fetch tasks through ids.
     *
     * @param taskIds
     * @return
     */
    public List<Task> findByIdIn(List<Long> taskIds);

}
