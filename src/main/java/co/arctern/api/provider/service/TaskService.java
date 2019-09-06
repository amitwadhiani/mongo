package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @return
     */
    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId);

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
    public Page<TasksForRider> fetchCompletedTasksForUser(Long userId, Pageable pageable);

    /**
     * fetch assigned tasks for user.
     *
     * @param userId
     * @return
     */
    public Page<TasksForRider> fetchAssignedTasksForUser(Long userId, Pageable pageable);

    /**
     * fetch cancelled tasks for user.
     *
     * @param userId
     * @return
     */
    public List<TasksForRider> fetchCancelledTasksForUser(Long userId, Pageable pageable);

    /**
     * reassign task to new user or cancel task.
     * cumulative service call.
     *
     * @param userId
     * @param task
     */
    public void markInactiveAndReassignTask(Long userId, Task task);
}
