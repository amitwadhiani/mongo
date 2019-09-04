package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.util.MessageUtil;

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
    public void markInactive(Task task);

    /**
     * fetch tasks for user.
     *
     * @param userId
     * @param state
     * @return
     */
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state);

    /**
     * find active user-task ( only one possible at a time for particular task ).
     *
     * @param taskId
     * @return
     */
    public UserTask findActiveUserTask(Long taskId);

}
