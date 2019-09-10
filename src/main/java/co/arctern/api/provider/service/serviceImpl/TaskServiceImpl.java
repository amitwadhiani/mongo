package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private TaskStateFlowService taskStateFlowService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ReasonService reasonService;

    @Autowired
    private ProjectionFactory projectionFactory;

    @Override
    public Task fetchTask(Long taskId) {
        return taskDao.findById(taskId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_TASK_ID_MESSAGE);
        });
    }

    @Override
    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto) {
        Task task = new Task();
        Long userId = dto.getUserId();
        task.setIsPrepaid(dto.getIsPrepaid());
        task.setPatientPhone(dto.getPatientPhone());
        task.setPatientName(dto.getPatientName());
        task.setType(dto.getType());
        task.setDestinationAddress(addressService.createOrFetchAddress(dto));
        task.setSourceAddress(addressService.createOrFetchAddress(dto));
        task.setState(TaskState.ASSIGNED);
        task = taskDao.save(task);
        paymentService.create(task, dto);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskStateFlowService.createFlow(task, TaskEventFlowState.OPEN, userId);
        taskStateFlowService.createFlow(task, TaskEventFlowState.ASSIGNED, userId);
        return TASK_ASSIGNED_MESSAGE;
    }

    @Override
    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskEventFlowState state) {
        Task task = fetchTask(taskId);
        taskStateFlowService.createFlow(task, state, userTaskService.findActiveUserTask(taskId).getUser().getId());
        task.setState((state.equals(TaskEventFlowState.ACCEPTED) ? TaskState.ACCEPTED : TaskState.OPEN));
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder reassignTask(Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        markInactiveAndReassignTask(userId, task);
        return TASK_REASSIGN_MESSAGE;
    }

    @Override
    public void markInactiveAndReassignTask(Long userId, Task task) {
        userTaskService.markInactive(task);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskStateFlowService.createFlow(task, TaskEventFlowState.REASSIGNED, userId);
        task.setState(TaskState.ASSIGNED);
        taskDao.save(task);
    }

    @Override
    @Transactional
    public StringBuilder startTask(Long taskId, Long userId) {
        Task task = fetchTask(taskId);
        task.setState(TaskState.STARTED);
        taskStateFlowService.createFlow(task, TaskEventFlowState.STARTED, userId);
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder rescheduleTask(Long taskId, Long userId, Timestamp time) {
        Task task = fetchTask(taskId);
        task.setState(TaskState.OPEN);
        task.setExpectedArrivalTime(time);
        taskStateFlowService.createFlow(task, TaskEventFlowState.RESCHEDULED, userId);
        taskDao.save(task);
        return SUCCESS_MESSAGE;

    }

    @Override
    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        if (isCancelled) {
            /**
             *  cancel
             */
            task.setIsActive(false);
            taskStateFlowService.createFlow(task, TaskEventFlowState.CANCELLED,
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
    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId, List<Long> reasonIds) {
        Task task = this.fetchTask(taskId);
        task.setCancellationRequested(cancelRequest);
        reasonService.assignReasons(task, reasonIds);
        return SUCCESS_MESSAGE;
    }

    @Override
    public Page<TasksForProvider> fetchCompletedTasksForUser(Long userId, Pageable pageable) {
        return userTaskService.fetchTasksForUser(userId, TaskState.COMPLETED, pageable)
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()));
    }

    @Override
    public Page<TasksForProvider> fetchAssignedTasksForUser(Long userId, Pageable pageable) {
        return userTaskService.fetchTasksForUser(userId, TaskState.ASSIGNED, pageable)
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()));
    }

    @Override
    public List<TasksForProvider> fetchCancelledTasksForUser(Long userId, Pageable pageable) {
        return userTaskService.fetchTasksForUser(userId, TaskState.CANCELLED, pageable).stream()
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TasksForProvider> fetchTasksByType(OfferingType type, Pageable pageable) {
        return taskDao.findByType(type, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, Pageable pageable) {
        return taskDao.findByDestinationAddressAreaIdIn(areaIds, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public Page<TasksForProvider> fetchTasksByType(OfferingType type, Timestamp start, Timestamp end, Pageable pageable) {
        return taskDao.findByTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(type, start, end, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable) {
        return taskDao.findByDestinationAddressAreaIdInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(areaIds, start, end, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

}
