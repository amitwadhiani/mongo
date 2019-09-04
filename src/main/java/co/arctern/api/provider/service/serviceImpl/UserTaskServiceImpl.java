package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dao.UserTaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserTaskServiceImpl implements UserTaskService {

    @Autowired
    private UserTaskDao userTaskDao;

    public void createUserTask(User user, Task task) {
        UserTask userTask = new UserTask();
        userTask.setTask(task);
        userTask.setIsActive(true);
        userTask.setUser(user);
        userTaskDao.save(userTask);
    }

    public void markInactive(Task task) {
        UserTask userTask = userTaskDao.findByIsActiveTrueAndTaskId(task.getId());
        userTask.setIsActive(false);
        userTaskDao.save(userTask);
    }

    public Page<UserTask> fetchTasksForUser(Long userId, TaskState state, Pageable pageable) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateOrderByCreatedAtDesc(userId, state, pageable);
    }

    public UserTask findActiveUserTask(Long taskId) {
        return userTaskDao.findByIsActiveTrueAndTaskId(taskId);
    }
}
