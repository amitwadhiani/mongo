package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dao.UserTaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserTaskService;
import co.arctern.api.provider.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
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
    TaskService taskService;

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
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskStateAndCreatedAtGreaterThanEqual(userId, state, start);
    }

    @Override
    public List<UserTask> fetchTasksForUser(Long userId, TaskState state) {
        return userTaskDao.findByIsActiveTrueAndUserIdAndTaskState(userId, state);
    }

    @Override
    public PaginatedResponse fetchTasks(TaskState[] states,
                                        List<Long> areaIds, TaskType taskType,
                                        Long orderId,
                                        String patientFilterValue,
                                        Pageable pageable) {
        if (states == null) {
            states = new TaskState[]{TaskState.OPEN, TaskState.ASSIGNED, TaskState.STARTED, TaskState.COMPLETED,
                    TaskState.ACCEPTED, TaskState.CANCELLED};
        }
        if (StringUtils.isEmpty(patientFilterValue)) {
            if (areaIds == null) {
                if (orderId == null) {
                    return getPaginatedResponse(
                            taskService.findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(states, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        taskService.findByIsActiveTrueAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(states, orderId, taskType, pageable), pageable);
            } else {
                if (orderId == null) {
                    return getPaginatedResponse(
                            taskService.findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(areaIds, states, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        taskService.findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(areaIds, states, orderId, taskType, pageable), pageable);
            }
        } else {
            if (areaIds == null) {
                if (orderId == null) {
                    return getPaginatedResponse(
                            taskService.filterByPatientDetailsWoAreaIds(states, patientFilterValue, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        taskService.filterByPatientDetailsWoAreaIds(states, orderId, patientFilterValue, taskType, pageable), pageable);
            } else {
                if (orderId == null) {
                    return getPaginatedResponse(
                            taskService.filterByPatientDetailsWithAreaIds(areaIds, states, patientFilterValue, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        taskService.filterByPatientDetailsWithAreaIdsAndOrderId(areaIds, orderId, states, patientFilterValue, taskType, pageable), pageable);
            }
        }
    }

    public PaginatedResponse getPaginatedResponse(Page<Task> tasks, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(tasks.map(a -> projectionFactory.createProjection(TasksForProvider.class, a)), pageable);
    }


}
