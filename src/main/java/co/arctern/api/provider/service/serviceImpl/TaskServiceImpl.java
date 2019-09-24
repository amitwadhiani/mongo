package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskStateFlowState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.util.DateUtil;
import co.arctern.api.provider.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
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

    private final TaskDao taskDao;
    private final UserTaskService userTaskService;
    private final TaskStateFlowService taskStateFlowService;
    private final UserService userService;
    private final AddressService addressService;
    private final PaymentService paymentService;
    private final ReasonService reasonService;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public TaskServiceImpl(TaskDao taskDao,
                           UserTaskService userTaskService,
                           TaskStateFlowService taskStateFlowService,
                           UserService userService,
                           AddressService addressService,
                           PaymentService paymentService,
                           ReasonService reasonService,
                           ProjectionFactory projectionFactory) {
        this.taskDao = taskDao;
        this.userTaskService = userTaskService;
        this.taskStateFlowService = taskStateFlowService;
        this.userService = userService;
        this.addressService = addressService;
        this.paymentService = paymentService;
        this.reasonService = reasonService;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public Task fetchTask(Long taskId) {
        return taskDao.findById(taskId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_TASK_ID_MESSAGE.toString());
        });
    }

    @Override
    @Transactional
    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto) {
        Long userId = dto.getUserId();
        Task task = createTask(dto);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.OPEN, userId);
        taskStateFlowService.createFlow(task, TaskStateFlowState.ASSIGNED, userId);
        return TASK_ASSIGNED_MESSAGE;
    }

    @Override
    @Transactional
    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskStateFlowState state) {
        Task task = fetchTask(taskId);
        if (state.equals(TaskStateFlowState.REJECTED)) {
            userTaskService.markInactive(task);
        }
        taskStateFlowService.createFlow(task, state, userTaskService.findActiveUserTask(taskId).getUser().getId());
        task.setState((state.equals(TaskStateFlowState.ACCEPTED) ? TaskState.ACCEPTED : TaskState.OPEN));
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
        taskStateFlowService.createFlow(task, TaskStateFlowState.ASSIGNED, userId);
        return TASK_ASSIGNED_MESSAGE;
    }

    @Override
    @Transactional
    public void markInactiveAndReassignTask(Long userId, Task task) {
        userTaskService.markInactive(task);
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.REASSIGNED, userId);
        task.setState(TaskState.ASSIGNED);
        taskDao.save(task);
    }

    @Override
    @Transactional
    public StringBuilder startTask(Long taskId, Long userId) {
        Task task = fetchTask(taskId);
        if (task.getUserTasks().stream().filter(UserTask::getIsActive).findFirst().get().getUser().getId().longValue() != userId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TASK_NOT_ASSIGNED_OR_INACTIVE_MESSAGE.toString());
        }
        task.setState(TaskState.STARTED);
        taskStateFlowService.createFlow(task, TaskStateFlowState.STARTED, userId);
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
        taskStateFlowService.createFlow(task, TaskStateFlowState.RESCHEDULED, userId);
        taskDao.save(task);
        return SUCCESS_MESSAGE;

    }

    @Override
    @Transactional
    public StringBuilder completeTask(Long taskId, Long userId) {
        Task task = fetchTask(taskId);
        task.setState(TaskState.COMPLETED);
        taskStateFlowService.createFlow(task, TaskStateFlowState.COMPLETED, userId);
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
            taskStateFlowService.createFlow(task, TaskStateFlowState.CANCELLED,
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
        return PaginationUtil.returnPaginatedBody(taskDao.findByCancellationRequestedTrue(pageable).map(task -> projectionFactory.createProjection(TasksForProvider.class, task)), pageable);
    }


    @Override
    public Task createTask(TaskAssignDto dto) {
        Long taskId = dto.getTaskId();
        Task task = (taskId == null) ? new Task() : taskDao.findById(taskId).orElseThrow(() ->
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_TASK_ID_MESSAGE.toString());
        });
        /**
         * to set source address Id of Warehouse / provider ( 3 in case of staging , change later / on prod.
         */
        dto.setSourceAddressId(3l);
        task.setIsPrepaid(dto.getIsPrepaid());
        task.setPatientPhone(dto.getPatientPhone());
        task.setPatientName(dto.getPatientName());
        task.setPatientId(dto.getPatientId());
        task.setType(dto.getType());
        task.setRefId(dto.getRefId());
        task.setCancellationRequested(false);
        task.setIsActive(true);
        task.setExpectedArrivalTime(dto.getExpectedArrivalTime());
        task.setDestinationAddress(addressService.createOrFetchAddress(dto, dto.getDestAddressId()));
        task.setSourceAddress(addressService.createOrFetchAddress(dto, dto.getSourceAddressId()));
        task.setState(TaskState.OPEN);
        String paymentMode = dto.getPaymentMode();
        if (paymentMode == null || paymentMode.isEmpty()) dto.setPaymentMode("");
        task = taskDao.save(task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.OPEN, null);
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
    public List<TasksForProvider> fetchTasksForUser(Long userId, TaskState state) {
        return userTaskService.fetchTasksForUser(userId, state)
                .stream()
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Task> findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, Timestamp start, Timestamp end, TaskType type, Pageable pageable) {
        return taskDao.findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(states, type, start, end, pageable);
    }

    @Override
    public Page<Task> filterByPatientDetailsWoAreaIds(
            TaskState[] states, Timestamp start, Timestamp end, String value, TaskType type, Pageable pageable) {
        return taskDao.filterByPatientDetailsAndTime(states, value, type, start, end, pageable);
    }

    @Override
    public Page<Task> filterByPatientDetailsWoAreaIds(
            TaskState[] states, Timestamp start, Timestamp end, Long orderId, String value, TaskType type, Pageable pageable) {
        return taskDao.filterByPatientDetailsWithRefIdWithTime(states, orderId, value, type, start, end, pageable);
    }

    @Override
    public Page<Task> filterByPatientDetailsWithAreaIds(List<Long> areaIds, Timestamp start, Timestamp end, TaskState[] states, String value, TaskType type, Pageable pageable) {
        return taskDao.filterByAreaIdsAndPatientDetailsWithTime(areaIds, states, type, value, start, end, pageable);
    }

    @Override
    public Page<Task> filterByPatientDetailsWithAreaIdsAndOrderId(List<Long> areaIds, Timestamp start, Timestamp end, Long orderId, TaskState[] states, String value, TaskType type, Pageable pageable) {
        return taskDao.filterByAreaIdsAndPatientDetailsWithRefIdWithTime(areaIds, orderId, states, type, value, start, end, pageable);
    }

    @Override
    public Page<Task> findByIsActiveTrueAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            TaskState[] states, Timestamp start, Timestamp end, Long orderId, TaskType type, Pageable pageable) {
        return taskDao.findByIsActiveTrueAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(states, orderId, type, start, end, pageable);
    }

    @Override
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, Timestamp start, Timestamp end, TaskState[] states, Long refId, TaskType type, Pageable pageable) {
        return taskDao.findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                areaIds, states, refId, type, start, end, pageable);
    }

    @Override
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, Timestamp start, Timestamp end, TaskState[] states, TaskType type, Pageable pageable) {
        return taskDao.findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                areaIds, states, type, start, end, pageable);
    }

    @Override
    public PaginatedResponse fetchTasks(TaskState[] states,
                                        Timestamp start, Timestamp end,
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
                            this.findByIsActiveTrueAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(states, start, end, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        this.findByIsActiveTrueAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(states, start, end, orderId, taskType, pageable), pageable);
            } else {
                if (orderId == null) {
                    return getPaginatedResponse(
                            this.findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(areaIds, start, end, states, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        this.findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(areaIds, start, end, states, orderId, taskType, pageable), pageable);
            }
        } else {
            if (areaIds == null) {
                if (orderId == null) {
                    return getPaginatedResponse(
                            this.filterByPatientDetailsWoAreaIds(states, start, end, patientFilterValue, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        this.filterByPatientDetailsWoAreaIds(states, start, end, orderId, patientFilterValue, taskType, pageable), pageable);
            } else {
                if (orderId == null) {
                    return getPaginatedResponse(
                            this.filterByPatientDetailsWithAreaIds(areaIds, start, end, states, patientFilterValue, taskType, pageable), pageable);
                }
                return getPaginatedResponse(
                        this.filterByPatientDetailsWithAreaIdsAndOrderId(areaIds, start, end, orderId, states, patientFilterValue, taskType, pageable), pageable);
            }
        }
    }

    public PaginatedResponse getPaginatedResponse(Page<Task> tasks, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(tasks.map(task -> projectionFactory.createProjection(TasksForProvider.class, task)), pageable);
    }

    public List<Task> fetchTasksForCron() {
        return taskDao.findByIsActiveTrueAndCreatedAtLessThanEqualAndState(DateUtil.fetchTimestampFromCurrentTimestamp(20), TaskState.ASSIGNED);
    }

    public Iterable<Task> saveAll(List<Task> tasks) {
        return taskDao.saveAll(tasks);
    }
}
