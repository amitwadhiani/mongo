package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.util.PaginationUtil;
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
    @Transactional
    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto) {
        Long userId = dto.getUserId();
        Task task = createTask(dto);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskStateFlowService.createFlow(task, TaskEventFlowState.OPEN, userId);
        taskStateFlowService.createFlow(task, TaskEventFlowState.ASSIGNED, userId);
        return TASK_ASSIGNED_MESSAGE;
    }

    @Override
    @Transactional
    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskEventFlowState state) {
        Task task = fetchTask(taskId);
        taskStateFlowService.createFlow(task, state, userTaskService.findActiveUserTask(taskId).getUser().getId());
        task.setState((state.equals(TaskEventFlowState.ACCEPTED) ? TaskState.ACCEPTED : TaskState.OPEN));
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    @Transactional
    public StringBuilder reassignTask(Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        markInactiveAndReassignTask(userId, task);
        return TASK_REASSIGN_MESSAGE;
    }

    /**
     * assign task to user.
     *
     * @param taskId
     * @param userId
     * @return
     */
    @Override
    public StringBuilder assignTask(Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        task.setState(TaskState.ASSIGNED);
        task = taskDao.save(task);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskStateFlowService.createFlow(task, TaskEventFlowState.ASSIGNED, userId);
        return TASK_ASSIGNED_MESSAGE;
    }

    @Override
    @Transactional
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
        if (task.getUserTasks().stream().filter(a -> a.getIsActive()).findFirst().get().getUser().getId().longValue() != userId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not assigned/active for this user.");
        }
        task.setState(TaskState.STARTED);
        taskStateFlowService.createFlow(task, TaskEventFlowState.STARTED, userId);
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    @Transactional
    public StringBuilder rescheduleTask(Long taskId, Long userId, Timestamp time) {
        Task task = fetchTask(taskId);
        task.setState(TaskState.OPEN);
        task.setExpectedArrivalTime(time);
        userTaskService.markInactive(task);
        taskStateFlowService.createFlow(task, TaskEventFlowState.RESCHEDULED, userId);
        taskDao.save(task);
        return SUCCESS_MESSAGE;

    }

    @Override
    @Transactional
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
    @Transactional
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

    @Override
    public PaginatedResponse seeCancelRequests(Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskDao.findByCancellationRequestedTrue(pageable).map(a -> projectionFactory.createProjection(TasksForProvider.class, a)), pageable);
    }


    @Override
    public Task createTask(TaskAssignDto dto) {
        Long taskId = dto.getTaskId();
        Task task = (taskId == null) ? new Task() : taskDao.findById(taskId).orElseThrow(() ->
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task Id.");
        });
        task.setIsPrepaid(dto.getIsPrepaid());
        task.setPatientPhone(dto.getPatientPhone());
        task.setPatientName(dto.getPatientName());
        task.setType(dto.getType());
        task.setRefId(dto.getRefId());
        task.setCancellationRequested(false);
        task.setIsActive(true);
        task.setExpectedArrivalTime(dto.getExpectedArrivalTime());
        task.setDestinationAddress(addressService.createOrFetchAddress(dto, dto.getDestAddressId()));
        task.setSourceAddress(addressService.createOrFetchAddress(dto, dto.getSourceAddressId()));
        task.setState(TaskState.OPEN);
        task = taskDao.save(task);
        taskStateFlowService.createFlow(task, TaskEventFlowState.OPEN, null);
        paymentService.create(task, dto);
        return task;

    }

    @Override
    public TasksForProvider fetchProjectedResponseFromPost(TaskAssignDto dto) {
        return projectionFactory.createProjection(TasksForProvider.class, createTask(dto));
    }

    @Override
    public List<TasksForProvider> fetchTasksForUser(Long userId, TaskState state, Timestamp start, Timestamp end) {
        return userTaskService.fetchTasksForUser(userId, state, start, end)
                .stream()
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TasksForProvider> fetchUpcomingTasksForUser(Long userId, TaskState state, Timestamp start) {
        return userTaskService.fetchTasksForUser(userId, state, start)
                .stream()
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Task> findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable) {
        return taskDao.findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(states, type, start, end, pageable);
    }

    @Override
    public Page<Task> filterByPatientDetailsWoAreaIds(
            TaskState[] states, String value, TaskType type, Timestamp start, Timestamp end, Pageable pageable) {
        return taskDao.filterByPatientDetails(states, value, type, start, end, pageable);
    }

    @Override
    public Page<Task> filterByPatientDetailsWithAreaIds(List<Long> areaIds, TaskState[] states, String value, TaskType type, Timestamp start, Timestamp end, Pageable pageable) {
        return taskDao.filterByAreaIdsAndPatientDetails(areaIds, states, type, value, start, end, pageable);
    }

}
