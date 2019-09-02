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
        task.setUser(userService.fetchUser(userId));
        task.setAmount(dto.getAmount());
        task.setPaymentState(dto.getPaymentState());
        task.setDiagnosticOrderId(dto.getDiagnosticOrderId());
        taskEventService.createFlow(taskDao.save(task), TaskEventState.OPEN, userId);
        taskEventService.createFlow(taskDao.save(task), TaskEventState.ACCEPTED, userId);
        return TASK_ACCEPT_MESSAGE;
    }

    @Override
    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskEventState state) {
        Task task = fetchTask(taskId);
        taskEventService.createFlow(task, state, task.getUser().getId());
        task.setState((state.equals(TaskEventState.ACCEPTED) ? TaskState.ACCEPTED : TaskState.OPEN));
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder reassignTask(Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        task.setUser(userService.fetchUser(userId));
        task.setIsActive(true);
        task.setCancellationRequested(false);
        taskEventService.createFlow(task, TaskEventState.REASSIGNED, userId);
        task.setState(TaskState.ASSIGNED);
        taskDao.save(task);
        return TASK_REASSIGN_MESSAGE;
    }

    @Override
    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        if (isCancelled) {
            /**
             *  cancel
             */
            task.setIsActive(false);
            taskEventService.createFlow(task, TaskEventState.CANCELLED, task.getUser().getId());
            task.setState(TaskState.CANCELLED);
            taskDao.save(task);
            return TASK_CANCEL_MESSAGE;
        } else {
            /**
             * reassign
             */
            task.setUser(userService.fetchUser(userId));
            taskEventService.createFlow(task, TaskEventState.REASSIGNED, userId);
            task.setState(TaskState.ASSIGNED);
            taskDao.save(task);
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
        return taskDao.findByStateAndUserId(TaskState.COMPLETED, userId).stream()
                .map(a -> projectionFactory.createProjection(TasksForRider.class, a))
                .collect(Collectors.toList());
    }

    @Override
    public List<TasksForRider> fetchAssignedTasksForUser(Long userId) {
        return taskDao.findByStateAndUserId(TaskState.ACCEPTED, userId).stream()
                .map(a -> projectionFactory.createProjection(TasksForRider.class, a))
                .collect(Collectors.toList());
    }

    @Override
    public List<TasksForRider> fetchCancelledTasksForUser(Long userId) {
        return taskDao.findByStateAndUserId(TaskState.CANCELLED, userId).stream()
                .map(a -> projectionFactory.createProjection(TasksForRider.class, a))
                .collect(Collectors.toList());
    }


}
