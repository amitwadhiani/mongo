package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dao.UserTaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.service.UserTaskService;
import co.arctern.api.provider.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserTaskServiceImpl implements UserTaskService {

    private final UserTaskDao userTaskDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public UserTaskServiceImpl(UserTaskDao userTaskDao,
                               ProjectionFactory projectionFactory) {
        this.userTaskDao = userTaskDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public void createUserTask(User user, Task task) {
        UserTask userTask = new UserTask();
        userTask.setTask(task);
        userTask.setIsActive(true);
        userTask.setUser(user);
        userTaskDao.save(userTask);
    }

    @Override
    public Long markInactive(Task task) {
        UserTask userTask = userTaskDao.findByIsActiveTrueAndTaskId(task.getId());
        if (userTask != null) {
            userTask.setIsActive(false);
            userTaskDao.save(userTask);
            return userTask.getUser().getId();
        }
        return null;
    }

    @Override
    public Page<UserTask> fetchTasksForUser(Long userId, TaskState[] states, Pageable pageable) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateInOrderByTaskCreatedAtDesc(userId, states, pageable);
    }

    @Override
    public Page<UserTask> fetchTasksForUser(Long userId, TaskState[] states, TaskType type, Pageable pageable) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateInAndTaskTypeOrderByTaskCreatedAtDesc(userId, states, type, pageable);
    }

    @Override
    public Page<UserTask> fetchTasksForUser(Long userId, TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateInAndTaskTypeAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThanOrderByTaskCreatedAtDesc(userId, states, type, start, end, pageable);
    }

    @Override
    public UserTask findActiveUserTask(Long taskId) {
        return userTaskDao.findByIsActiveTrueAndTaskId(taskId);
    }

    @Override
    public User findActiveUserFromUserTask(Long taskId) {
        UserTask userTask = userTaskDao.findByIsActiveTrueAndTaskId(taskId);
        return (userTask == null) ? null : userTask.getUser();

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
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateAndCreatedAtGreaterThanEqual(userId, state, start);
    }

    @Override
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateOrderByCreatedAtDesc(userId, state);
    }

    @Override
    public List<UserTask> fetchUserTasksForCron() {
        return userTaskDao.fetchUserTasksForCron(DateUtil.fetchTimestampFromCurrentTimestamp(20), TaskState.ASSIGNED);
    }

    @Override
    public Page<UserTask> fetchTasksForProvider(List<Long> ids, TaskState state, TaskType type, Pageable pageable) {
        return userTaskDao.findByIsActiveTrueAndUserIdInAndTaskType(ids, type, pageable);
    }

    @Override
    public Page<UserTask> fetchTasksForProvider(List<Long> ids, TaskState state, TaskType type, Timestamp start, Timestamp end, Pageable pageable) {
        return userTaskDao.findByIsActiveTrueAndUserIdInAndTaskTypeAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(ids, type, start, end, pageable);
    }


}
