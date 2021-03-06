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
    Page<Task> findByTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(OfferingType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks for a particular area.
     *
     * @param areaIds
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdInOrderByExpectedArrivalTime(List<Long> areaIds, Pageable pageable);

    /**
     * fetch tasks by destination address ids and taskType.
     *
     * @param areaIds
     * @param type
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdInAndTypeOrderByExpectedArrivalTime(List<Long> areaIds, TaskType type, Pageable pageable);

    /**
     * fetch tasks for a particular area within a time range.
     *
     * @param areaIds
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdInAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch by destination area ids and taskType within a date range.
     *
     * @param areaIds
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByDestinationAddressAreaIdInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(List<Long> areaIds, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch cancellation requested tasks.
     *
     * @param pageable
     * @return
     */
    Page<Task> findByCancellationRequestedTrueOrderByLastModifiedAtDesc(Pageable pageable);


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
    Page<Task> findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(
            TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch active tasks through states,type within arrival date range.
     *
     * @param states
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<Task> findByIsActiveTrueAndStateInAndTypeAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(
            TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks filtered by states and type.
     *
     * @param states
     * @param type
     * @param pageable
     * @return
     */
    Page<Task> findByIsActiveTrueAndStateInAndTypeOrderByExpectedArrivalTime(
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
            "AND task.expectedArrivalTime >= (:start) " +
            "AND task.expectedArrivalTime < (:end) " +
            "ORDER BY task.expectedArrivalTime ASC ")
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
            "ORDER BY task.expectedArrivalTime ASC ")
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
            "AND task.expectedArrivalTime >= (:start) " +
            "AND task.expectedArrivalTime < (:end) " +
            "ORDER BY task.expectedArrivalTime ASC ")
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
            "ORDER BY task.expectedArrivalTime ASC ")
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
            "AND task.expectedArrivalTime >= (:start) " +
            "AND task.expectedArrivalTime < (:end) " +
            "ORDER BY task.expectedArrivalTime ASC ")
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
            "ORDER BY task.expectedArrivalTime ASC ")
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
            "AND task.expectedArrivalTime >= (:start) " +
            "AND task.expectedArrivalTime < (:end) " +
            "ORDER BY task.expectedArrivalTime ASC ")
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
            "ORDER BY task.expectedArrivalTime ASC ")
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
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(
            List<Long> areaIds, TaskType type, TaskState[] states, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch active tasks through areaIds,type, states within a given date range.
     *
     * @param areaIds
     * @param type
     * @param states
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(
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
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInOrderByExpectedArrivalTime(
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
    Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(
            List<Long> areaIds, TaskState[] states, Long refId, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch active tasks by areaIds, states, refId,type within a given arrival date range.
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
    Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(
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
    Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeOrderByExpectedArrivalTime(
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
    public Page<Task> findByIsActiveTrueAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(
            TaskState[] states, Long orderId, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch active tasks by state,type,refId within arrival date range.
     *
     * @param states
     * @param orderId
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<Task> findByIsActiveTrueAndStateInAndRefIdAndTypeAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(
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
    public Page<Task> findByIsActiveTrueAndStateInAndRefIdAndTypeOrderByExpectedArrivalTime(
            TaskState[] states, Long orderId, TaskType type, Pageable pageable);

    /**
     * fetch filtered tasks by state and less than a datetime (for cronjon ).
     *
     * @param start
     * @param state
     * @return
     */
    public List<Task> findByIsActiveTrueAndCreatedAtLessThanEqualAndStateOrderByExpectedArrivalTime(Timestamp start, TaskState state);

    /**
     * fetch tasks through ids.
     *
     * @param taskIds
     * @return
     */
    public List<Task> findByIdIn(List<Long> taskIds);

    /**
     * fetch tasks by userId and state.
     *
     * @param userId
     * @param state
     * @return
     */
    @Query("FROM Task task " +
            "JOIN FETCH task.payments " +
            "WHERE task.activeUserId = :userId " +
            "AND task.state = :state ")
    public List<Task> fetchTasks(Long userId, TaskState state);

    /**
     * fetch tasks by userId and state.
     *
     * @param userId
     * @param state
     * @param pageable
     * @return
     */
    @Query("FROM Task task " +
            "WHERE task.activeUserId = :userId " +
            "AND task.state = :state " +
            "ORDER BY task.expectedArrivalTime DESC ")
    public Page<Task> fetchTasksForUser(Long userId, TaskState state, Pageable pageable);

}
