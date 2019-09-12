package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dao.UserTaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.UserTaskService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserTaskServiceImpl implements UserTaskService {

    @Autowired
    private UserTaskDao userTaskDao;

    @Autowired
    ProjectionFactory projectionFactory;

    @Override
    public void createUserTask(User user, Task task) {
        UserTask userTask = new UserTask();
        userTask.setTask(task);
        userTask.setIsActive(true);
        userTask.setUser(user);
        userTaskDao.save(userTask);
    }

    @Override
    public void markInactive(Task task) {
        UserTask userTask = userTaskDao.findByIsActiveTrueAndTaskId(task.getId());
        userTask.setIsActive(false);
        userTaskDao.save(userTask);
    }

    @Override
    public Page<UserTask> fetchTasksForUser(Long userId, TaskState state, Pageable pageable) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateOrderByTaskCreatedAtDesc(userId, state, pageable);
    }

    @Override
    public UserTask findActiveUserTask(Long taskId) {
        return userTaskDao.findByIsActiveTrueAndTaskId(taskId);
    }

    @Override
    public Long countByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(Long userId, TaskState state, Timestamp start, Timestamp end) {
        return userTaskDao.countByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(userId,
                state, start, end);
    }

    @Override
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state, Timestamp start, Timestamp end) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(userId, state, start, end);
    }

    @Override
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state, Timestamp start) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqual(userId, state, start);
    }

    @Override
    public PaginatedResponse fetchTasks(TaskState[] states, Timestamp start, Timestamp end, Pageable pageable) {
        if (states == null)
            states = new TaskState[]{TaskState.OPEN, TaskState.ASSIGNED, TaskState.STARTED, TaskState.COMPLETED,
                    TaskState.ACCEPTED, TaskState.CANCELLED};
        return PaginationUtil.returnPaginatedBody(userTaskDao.findByIsActiveTrueAndTaskStateInAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(
                states, start, end, pageable).map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask())), pageable);
    }


}
