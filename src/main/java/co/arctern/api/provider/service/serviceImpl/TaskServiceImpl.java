package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskEventState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.service.TaskEventService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserService;
import co.arctern.api.provider.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    TaskEventService taskEventService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectionFactory projectionFactory;

    @Override
    public Task fetchTask(Long taskId) {
        return taskDao.findById(taskId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid taskId");
        });
    }

    @Override
    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto) {
        Task task = new Task();
        Long userId = dto.getUserId();
        task.setAmount(dto.getAmount());
        task.setPaymentState(dto.getPaymentState());
        task.setDiagnosticOrderId(dto.getDiagnosticOrderId());
        task = taskDao.save(task);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskEventService.createFlow(task, TaskEventState.OPEN, userId);
        taskEventService.createFlow(task, TaskEventState.ACCEPTED, userId);
        return TASK_ACCEPT_MESSAGE;
    }

    @Override
    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskEventState state) {
        Task task = fetchTask(taskId);
        taskEventService.createFlow(task, state, userTaskService.findActiveUserTask(taskId).getUser().getId());
        task.setState((state.equals(TaskEventState.ACCEPTED) ? TaskState.ACCEPTED : TaskState.OPEN));
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder reassignTask(Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        markInactiveAndReassignTask(userId, task);
        return TASK_REASSIGN_MESSAGE;
    }

    public void markInactiveAndReassignTask(Long userId, Task task) {
        userTaskService.markInactive(task);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskEventService.createFlow(task, TaskEventState.REASSIGNED, userId);
        task.setState(TaskState.ASSIGNED);
        taskDao.save(task);
    }

    @Override
    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        if (isCancelled) {
            /**
             *  cancel
             */
            task.setIsActive(false);
            taskEventService.createFlow(task, TaskEventState.CANCELLED,
                    userTaskService.findActiveUserTask(taskId).getId());
            task.setState(TaskState.CANCELLED);
            taskDao.save(task);
            return TASK_CANCEL_MESSAGE;
        } else {
            /**
             * reassign
             */
            markInactiveAndReassignTask(userId, task);
            return TASK_REASSIGN_MESSAGE;
        }
    }

    @Override
    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId) {
        Task task = this.fetchTask(taskId);
        task.setCancellationRequested(cancelRequest);
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    public List<TasksForRider> fetchCompletedTasksForUser(Long userId) {
        return userTaskService.fetchTasksForUser(userId, TaskState.COMPLETED).stream()
                .map(a -> projectionFactory.createProjection(TasksForRider.class, a.getTask()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TasksForRider> fetchAssignedTasksForUser(Long userId) {
        return userTaskService.fetchTasksForUser(userId, TaskState.ASSIGNED).stream()
                .map(a -> projectionFactory.createProjection(TasksForRider.class, a.getTask()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TasksForRider> fetchCancelledTasksForUser(Long userId) {
        return userTaskService.fetchTasksForUser(userId, TaskState.CANCELLED).stream()
                .map(a -> projectionFactory.createProjection(TasksForRider.class, a.getTask()))
                .collect(Collectors.toList());
    }


}
