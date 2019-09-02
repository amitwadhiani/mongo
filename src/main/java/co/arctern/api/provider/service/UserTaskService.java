package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface UserTaskService extends MessageUtil {

    public void createUserTask(User user, Task task);

    public void markInactive(Task task);

    public List<UserTask> fetchTasksForUser(Long userId, TaskState state);

    public UserTask findActiveUserTask(Long taskId);

}
