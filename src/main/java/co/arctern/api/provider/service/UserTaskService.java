package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface UserTaskService extends MessageUtil {

    /**
     * map task with user.
     *
     * @param user
     * @param task
     */
    public void createUserTask(User user, Task task);

    /**
     * mark task inactive.
     *
     * @param task
     */
    public Long markInactive(Task task);

    /**
     * fetch tasks for user.
     *
     * @param userId
     * @param states
     * @return
     */
    public Page<UserTask> fetchTasksForUser(Long userId, TaskState[] states, Pageable pageable);

    /**
     * fetch tasks for a provider.
     *
     * @param userId
     * @param states
     * @param type
     * @param pageable
     * @return
     */
    public Page<UserTask> fetchTasksForUser(Long userId, TaskState[] states, TaskType type, Pageable pageable);

    /**
     * fetch tasks within a given time interval.
     *
     * @param userId
     * @param states
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<UserTask> fetchTasksForUser(Long userId, TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks for user.
     *
     * @param ids
     * @param state
     * @param type
     * @param pageable
     * @return
     */
    public Page<UserTask> fetchTasksForProvider(List<Long> ids, TaskState state, TaskType type, Pageable pageable);

    /**
     * fetch tasks.
     *
     * @param ids
     * @param state
     * @param type
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    public Page<UserTask> fetchTasksForProvider(List<Long> ids, TaskState state, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * find active user-task ( only one possible at a time for particular task ).
     *
     * @param taskId
     * @return
     */
    public UserTask findActiveUserTask(Long taskId);

    /**
     * fetch count of tasks for a user filtered by state within a range.
     *
     * @param userId
     * @param state
     * @param start
     * @param end
     * @return
     */
    public Long countByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(
            Long userId, TaskState state, Timestamp start, Timestamp end);

    /**
     * fetch tasks for a user within a time range.
     *
     * @param userId
     * @param state
     * @param start
     * @param end
     * @return
     */
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state, Timestamp start, Timestamp end);

    /**
     * fetch tasks for user filtered by state greater than a given time.
     *
     * @param userId
     * @param state
     * @param start
     * @return
     */
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state, Timestamp start);

    /**
     * fetch tasks for user.
     *
     * @param userId
     * @param state
     * @return
     */
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state);

    /**
     * fetch userTasks for cronjob.
     *
     * @return
     */
    public List<UserTask> fetchUserTasksForCron();

    /**
     * find active user for a task.
     *
     * @param taskId
     * @return
     */
    public User findActiveUserFromUserTask(Long taskId);

}
