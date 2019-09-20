package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface TaskService extends MessageUtil {

    /**
     * fetch task based on id.
     *
     * @param taskId
     * @return
     */
    public Task fetchTask(Long taskId);

    /**
     * create task and assign user to it .
     *
     * @param dto
     * @return
     */
    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto);

    /**
     * reassign task to new user.
     *
     * @param taskId
     * @param userId
     * @return
     */
    public StringBuilder reassignTask(Long taskId, Long userId);

    /**
     * assign task to user.
     *
     * @param taskId
     * @param userId
     * @return
     */
    public StringBuilder assignTask(Long taskId, Long userId);

    /**
     * cancel task and mark inactive -> By Admin.
     *
     * @param isCancelled
     * @param taskId
     * @param userId
     * @return
     */
    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId);

    /**
     * request cancellation for a particular task based on taskId -> By Rider.
     *
     * @param cancelRequest
     * @param taskId
     * @param reasonIds
     * @return
     */
    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId, List<Long> reasonIds);

    /**
     * accept/reject assigned task -> By rider.
     *
     * @param taskId
     * @param state
     * @return
     */
    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskEventFlowState state);

    /**
     * fetch completed tasks for user.
     *
     * @param userId
     * @return
     */
    public Page<TasksForProvider> fetchCompletedTasksForUser(Long userId, Pageable pageable);

    /**
     * fetch assigned tasks for user.
     *
     * @param userId
     * @return
     */
    public Page<TasksForProvider> fetchAssignedTasksForUser(Long userId, Pageable pageable);

    /**
     * fetch cancelled tasks for user.
     *
     * @param userId
     * @return
     */
    public List<TasksForProvider> fetchCancelledTasksForUser(Long userId, Pageable pageable);

    /**
     * reassign task to new user or cancel task.
     * cumulative service call.
     *
     * @param userId
     * @param task
     */
    public void markInactiveAndReassignTask(Long userId, Task task);

    /**
     * start an assigned task.
     *
     * @param taskId
     * @param userId
     * @return
     */
    public StringBuilder startTask(Long taskId, Long userId);

    /**
     * reschedule task and mark OPEN.
     *
     * @param taskId
     * @param userId
     * @param time
     * @return
     */
    public StringBuilder rescheduleTask(Long taskId, Long userId, Timestamp time);

    /**
     * fetch tasks by their offering type.
     *
     * @param type
     * @param pageable
     * @return
     */
    public Page<TasksForProvider> fetchTasksByType(OfferingType type, Pageable pageable);

    /**
     * fetch tasks filtered by area.
     *
     * @param areaIds
     * @param pageable
     * @return
     */
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, Pageable pageable);

    /**
     * fetch tasks by offering type within a time range.
     *
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<TasksForProvider> fetchTasksByType(OfferingType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks filtered by area within a time range.
     *
     * @param areaIds
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * see cancellation requests for Admin.
     *
     * @param pageable
     * @return
     */
    public PaginatedResponse seeCancelRequests(Pageable pageable);

    /**
     * create a new task .
     *
     * @param dto
     * @return
     */
    public Task createTask(TaskAssignDto dto);

    /**
     * fetch projected response after save call (task) .
     *
     * @param dto
     * @return
     */
    public TasksForProvider fetchProjectedResponseFromPost(TaskAssignDto dto);

    /**
     * fetch tasks with given state within a time range.
     *
     * @param userId
     * @param state
     * @param start
     * @param end
     * @return
     */
    public List<TasksForProvider> fetchTasksForUser(Long userId, TaskState state, Timestamp start, Timestamp end);

    /**
     * fetch upcoming tasks for a user filtered by state.
     *
     * @param userId
     * @param state
     * @param start
     * @return
     */
    public List<TasksForProvider> fetchUpcomingTasksForUser(Long userId, TaskState state, Timestamp start);

    /**
     * fetch all tasks for a user filtered by state.
     *
     * @param userId
     * @param state
     * @return
     */
    public List<TasksForProvider> fetchTasksForUser(Long userId, TaskState state);

    public Page<Task> findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, TaskState[] states, Long refId, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    public Page<Task> findByIsActiveTrueAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, Long orderId, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    public Page<Task> filterByPatientDetailsWoAreaIds(
            TaskState[] states, String value, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    public Page<Task> filterByPatientDetailsWoAreaIds(
            TaskState[] states, Long orderId, String value, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    public Page<Task> filterByPatientDetailsWithAreaIds(List<Long> areaIds, TaskState[] states, String value, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    public Page<Task> filterByPatientDetailsWithAreaIdsAndOrderId(List<Long> areaIds, Long orderId, TaskState[] states, String value, TaskType type, Timestamp start, Timestamp end, Pageable pageable);
}
