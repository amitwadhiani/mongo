package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.*;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskReason;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.dto.response.projection.Reasons;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.queue.Sender;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.util.DateUtil;
import co.arctern.api.provider.util.PaginationUtil;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    private final TokenService tokenService;
    private final ProjectionFactory projectionFactory;
    private final Sender sender;

    @Autowired
    public TaskServiceImpl(TaskDao taskDao,
                           UserTaskService userTaskService,
                           TaskStateFlowService taskStateFlowService,
                           UserService userService,
                           AddressService addressService,
                           PaymentService paymentService,
                           ReasonService reasonService,
                           ProjectionFactory projectionFactory,
                           TokenService tokenService,
                           Sender sender) {
        this.taskDao = taskDao;
        this.userTaskService = userTaskService;
        this.taskStateFlowService = taskStateFlowService;
        this.userService = userService;
        this.addressService = addressService;
        this.paymentService = paymentService;
        this.reasonService = reasonService;
        this.tokenService = tokenService;
        this.projectionFactory = projectionFactory;
        this.sender = sender;
    }

    @Override
    public Task fetchTask(Long taskId) {
        return taskDao.findById(taskId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_TASK_ID_MESSAGE.toString());
        });
    }

    @Override
    public List<Task> fetchTasks(List<Long> taskIds) {
        return taskDao.findByIdIn(taskIds);
    }

    @Override
    public List<Task> fetchAllTasks(List<Long> taskIds) {
        return taskDao.findByIdIn(taskIds);
    }

    @Override
    @Transactional
    public Task createTaskAndAssignUser(TaskAssignDto dto) {
        Long userId = tokenService.fetchUserId();
        User user = userService.fetchUser(userId);
        Task task = createTask(dto, userId, TaskState.STARTED);
        userTaskService.createUserTask((user), task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.ASSIGNED, userId);
        taskStateFlowService.createFlow(task, TaskStateFlowState.STARTED, userId);
        return task;
    }

    @SneakyThrows
    @Override
    @Transactional
    public StringBuilder acceptOrRejectAssignedTask(Long taskId, List<Long> reasonIds, TaskStateFlowState state) {
        Task task = fetchTask(taskId);
        Long userId = userTaskService.findActiveUserTask(taskId).getUser().getId();
        taskStateFlowService.createFlow(task, state, userId);
        task.setState((state.equals(TaskStateFlowState.ACCEPTED) ? TaskState.ACCEPTED : TaskState.REJECTED));
        if (state.equals(TaskStateFlowState.REJECTED)) {
            userTaskService.markInactive(task);
            reasonService.assignReasons(task, reasonIds, TaskStateFlowState.REJECTED);
        }
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
     * @param taskIds
     * @param userId
     * @return
     */
    @Override
    @Transactional
    @SneakyThrows
    public StringBuilder assignTasks(List<Long> taskIds, Long userId) {
        List<Task> tasks = this.fetchTasks(taskIds);
        for (Task task : tasks) {
            task.setState(TaskState.ASSIGNED);
            task.setActiveUserId(userId);
            sender.sendAdminAssignTaskNotification(userService.fetchUser(userId),
                    Long.valueOf(task.getPatientId()), task.getId());
        }
        tasks = Lists.newArrayList(taskDao.saveAll(tasks));
        tasks.stream().forEach(task -> {
            userTaskService.markInactive(task);
            userTaskService.createUserTask(userService.fetchUser(userId), task);
            taskStateFlowService.createFlow(task, TaskStateFlowState.ASSIGNED, userId);
        });
        return TASK_ASSIGNED_MESSAGE;
    }

    @Override
    @Transactional
    public void markInactiveAndReassignTask(Long userId, Task task) {
        Long taskUserId = userTaskService.markInactive(task);
        if (taskUserId != null && taskUserId.longValue() == userId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TASK_SAME_USER_MESSAGE.toString());
        }
        userTaskService.createUserTask(userService.fetchUser(userId), task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.REASSIGNED, userId);
        task.setState(TaskState.ASSIGNED);
        task.setCancellationRequested(false);
        task.setActiveUserId(userId);
        taskDao.save(task);
    }

    @SneakyThrows
    @Override
    @Transactional
    public StringBuilder startTask(Long taskId, Long userId) {
        Task task = fetchTask(taskId);
        if (task.getState().equals(TaskState.STARTED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TASK_ALREADY_STARTED_MESSAGE.toString());
        }
        if (task.getUserTasks().stream().filter(UserTask::getIsActive).findFirst().get().getUser().getId().longValue() != userId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TASK_NOT_ASSIGNED_OR_INACTIVE_MESSAGE.toString());
        }
        task.setState(TaskState.STARTED);
        taskStateFlowService.createFlow(task, TaskStateFlowState.STARTED, userId);
        taskDao.save(task);
        sender.sendTaskStateChangeNotification(userService.fetchUser(userId),TaskState.STARTED, Long.valueOf(task.getPatientId()), taskId);
        return SUCCESS_MESSAGE;
    }

    @SneakyThrows
    @Override
    @Transactional
    public StringBuilder rescheduleTask(Long taskId, Long userId, Timestamp time, Timestamp start, Timestamp end) {
        Task task = fetchTask(taskId);
        task.setState(TaskState.RESCHEDULED);
        task.setExpectedArrivalTime(time);
        /**
         * changed to start time and end time (in place of eta).
         */
        task.setStartTime(start);
        task.setEndTime(end);
        userTaskService.markInactive(task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.RESCHEDULED, userId);
        taskDao.save(task);
        sender.sendTaskStateChangeNotification(userService.fetchUser(userId),TaskState.RESCHEDULED, Long.valueOf(task.getPatientId()), taskId);
        return SUCCESS_MESSAGE;
    }

    @SneakyThrows
    @Override
    @Transactional
    public StringBuilder completeTask(Long taskId, Long userId) {
        Task task = fetchTask(taskId);
        task.setState(TaskState.COMPLETED);
        taskStateFlowService.createFlow(task, TaskStateFlowState.COMPLETED, userId);
        taskDao.save(task);
        sender.sendTaskStateChangeNotification(userService.fetchUser(userId),TaskState.COMPLETED, Long.valueOf(task.getPatientId()), taskId);
        return SUCCESS_MESSAGE;
    }

    @SneakyThrows
    @Override
    @Transactional
    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId, List<Long> reasonIds) {
        Task task = this.fetchTask(taskId);
        if (isCancelled) {
            if (task.getState().equals(TaskState.CANCELLED)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TASK_ALREADY_CANCELLED_MESSAGE.toString());
            }
            UserTask activeUserTask = userTaskService.findActiveUserTask(taskId);

            /**
             *  cancel
             */
            taskStateFlowService.createFlow(task, TaskStateFlowState.CANCELLED,
                    (activeUserTask == null) ? null : activeUserTask.getUser().getId());
            task.setState(TaskState.CANCELLED);
            task.setCancellationRequested(false);
            task = taskDao.save(task);
            if (!CollectionUtils.isEmpty(reasonIds))
                reasonService.assignReasons(task, reasonIds, TaskStateFlowState.CANCELLED);
            return TASK_CANCEL_MESSAGE;
        } else {
            /**
             * reassign
             */
            markInactiveAndReassignTask(userId, task);
            sender.sendTaskStateChangeNotification(userService.fetchUser(userId),TaskState.CANCELLED, Long.valueOf(task.getPatientId()), taskId);
            return TASK_REASSIGN_MESSAGE;
        }
    }

    @Override
    @Transactional
    public StringBuilder cancelAllTasks(List<Long> taskIds, Long userId) {
        List<Task> tasks = this.fetchAllTasks(taskIds);
        List<Task> tasksToSave = new ArrayList<>();
        tasks.stream().forEach(task ->
        {
            UserTask activeUserTask = userTaskService.findActiveUserTask(task.getId());
            /**
             *  cancel
             */
            taskStateFlowService.createFlow(task, TaskStateFlowState.CANCELLED,
                    (activeUserTask == null) ? null : activeUserTask.getUser().getId());
            task.setState(TaskState.CANCELLED);
            tasksToSave.add(task);
        });
        taskDao.saveAll(tasksToSave);
        return TASK_CANCEL_MESSAGE;
    }

    @Override
    @Transactional
    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId, List<Long> reasonIds) {
        Task task = this.fetchTask(taskId);
        task.setCancellationRequested(cancelRequest);
        reasonService.assignReasons(task, reasonIds, TaskStateFlowState.CANCELLATION_REQUESTED);
        return SUCCESS_MESSAGE;
    }

    @Override
    public Page<TasksForProvider> fetchCompletedTasksForUser(Long userId, Pageable pageable) {
        return userTaskService.fetchTasksForUser(userId, new TaskState[]{TaskState.COMPLETED}, pageable)
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()));
    }

    @Override
    public Page<TasksForProvider> fetchAssignedTasksForUser(Long userId, Pageable pageable) {
        return userTaskService.fetchTasksForUser(userId, new TaskState[]{TaskState.ASSIGNED}, pageable)
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()));
    }

    @Override
    public List<TasksForProvider> fetchCancelledTasksForUser(Long userId, Pageable pageable) {
        return userTaskService.fetchTasksForUser(userId, new TaskState[]{TaskState.CANCELLED}, pageable).stream()
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TasksForProvider> fetchTasksByType(OfferingType type, Pageable pageable) {
        return taskDao.findByType(type, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, TaskType taskType, Pageable pageable) {
        return taskDao.findByDestinationAddressAreaIdInAndTypeOrderByExpectedArrivalTime(areaIds, taskType, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public Page<TasksForProvider> fetchTasksByType(OfferingType type, Timestamp start, Timestamp end, Pageable pageable) {
        return taskDao.findByTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(type, start, end, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, TaskType taskType, Timestamp start, Timestamp end, Pageable pageable) {
        return taskDao.findByDestinationAddressAreaIdInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByExpectedArrivalTime(areaIds, taskType, start, end, pageable).map(
                a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public PaginatedResponse seeCancelRequests(Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskDao.findByCancellationRequestedTrueOrderByLastModifiedAtDesc(pageable).map(task -> projectionFactory.createProjection(TasksForProvider.class, task)), pageable);
    }


    @Override
    @Transactional
    public Task createTask(TaskAssignDto dto, Long userId, TaskState state) {
        Long taskId = dto.getTaskId();
        Task task = (taskId == null) ? new Task() : taskDao.findById(taskId).orElseThrow(() ->
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_TASK_ID_MESSAGE.toString());
        });
        task.setIsPrepaid(dto.getIsPrepaid());
        task.setPatientPhone(dto.getPatientPhone());
        task.setPatientName(dto.getPatientName());
        task.setPatientId(dto.getPatientId());
        task.setType(dto.getType());
        task.setRefId(dto.getRefId());
        task.setSource(dto.getSource());
        task.setCancellationRequested(false);
        task.setIsActive(true);
        task.setDiagnosticOrderId(dto.getDiagnosticOrderId());
        task.setExpectedArrivalTime((dto.getExpectedArrivalTime()) == null ?
                new Timestamp(System.currentTimeMillis() + (12 * 60 * 60 * 1000)) : dto.getExpectedArrivalTime());
        task.setDestinationAddress(addressService.createOrFetchAddress(dto, dto.getDestAddressId()));
        task.setSourceAddress(addressService.fetchSourceAddress());
        task.setState(state);
        task.setStartTime(dto.getStartTime());
        task.setEndTime(dto.getEndTime());
        if (userId != null) task.setActiveUserId(userId);
        String paymentMode = dto.getPaymentMode();
        if (paymentMode == null || paymentMode.isEmpty()) dto.setPaymentMode("");
        task = taskDao.save(task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.OPEN, null);
        paymentService.create(task, dto);
        return task;

    }

    @Override
    public TasksForProvider fetchProjectedResponseFromPost(TaskAssignDto dto) {
        return projectionFactory.createProjection(TasksForProvider.class, (dto.getFromExistingTask() == null || !dto.getFromExistingTask())
                ? this.createTask(dto, tokenService.fetchUserId(), TaskState.OPEN)
                : this.createTaskAndAssignUser(dto));
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
        return taskDao.findByIsActiveTrueAndStateInAndTypeAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(states, type, start, end, pageable);
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
        return taskDao.findByIsActiveTrueAndStateInAndRefIdAndTypeAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(states, orderId, type, start, end, pageable);
    }

    @Override
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, Timestamp start, Timestamp end, TaskState[] states, Long refId, TaskType type, Pageable pageable) {
        return taskDao.findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndRefIdAndTypeAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(
                areaIds, states, refId, type, start, end, pageable);
    }

    @Override
    public Page<Task> findByIsActiveTrueAndDestinationAddressAreaIdInAndStateInAndTypeAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            List<Long> areaIds, Timestamp start, Timestamp end, TaskState[] states, TaskType type, Pageable pageable) {
        return taskDao.findByIsActiveTrueAndDestinationAddressAreaIdInAndTypeAndStateInAndExpectedArrivalTimeGreaterThanEqualAndExpectedArrivalTimeLessThanOrderByExpectedArrivalTime(
                areaIds, type, states, start, end, pageable);
    }

    @Override
    public PaginatedResponse fetchTasks(TaskState[] states,
                                        Timestamp start, Timestamp end,
                                        List<Long> areaIds, TaskType taskType,
                                        Long orderId,
                                        String patientFilterValue,
                                        Long providerId,
                                        Pageable pageable) {
        if (states == null) {
            states = new TaskState[]{TaskState.OPEN, TaskState.ASSIGNED, TaskState.STARTED, TaskState.COMPLETED,
                    TaskState.ACCEPTED, TaskState.CANCELLED};
        }
        if (providerId != null) {
            return getPaginatedResponse((start != null && end != null) ?
                    userTaskService.fetchTasksForUser(providerId, states, taskType, start, end, pageable).map(a -> a.getTask()) :
                    userTaskService.fetchTasksForUser(providerId, states, taskType, pageable).map(a -> a.getTask()), pageable);
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

    @Override
    public PaginatedResponse getPaginatedResponse(Page<Task> tasks, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(tasks.map(task -> projectionFactory.createProjection(TasksForProvider.class, task)), pageable);
    }

    @Override
    public List<Task> fetchTasksForCron() {
        return taskDao.findByIsActiveTrueAndCreatedAtLessThanEqualAndStateOrderByExpectedArrivalTime(DateUtil.fetchTimestampFromCurrentTimestamp(20), TaskState.ASSIGNED);
    }

    @Override
    public Iterable<Task> saveAll(List<Task> tasks) {
        return taskDao.saveAll(tasks);
    }

    @Override
    public Page<TasksForProvider> fetchTasksByProvider(List<Long> ids, TaskType type, Pageable pageable) {
        return userTaskService.fetchTasksForProvider(ids, null, type, pageable).map(a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    public Page<TasksForProvider> fetchTasksByTypeAndProvider(List<Long> ids, TaskType type, Timestamp start, Timestamp end, Pageable pageable) {
        return userTaskService.fetchTasksForProvider(ids, null, type, start, end, pageable).map(a -> projectionFactory.createProjection(TasksForProvider.class, a));
    }

    @Override
    @Transactional
    public List<Payments> requestSettlement(Long userId, SettleState settleState) {
        List<Task> tasks = fetchTasksForPayment(userId);
        List<Payments> payments = new ArrayList<>();
        tasks.stream().forEach(a -> {
            payments.addAll(a.getPayments().stream().filter(b -> b.getSettleState().equals(settleState))
                    .map(c -> {
                        c.setPaidBy(userId);
                        c = paymentService.save(c);
                        paymentService.createSettleStateFlow(c, SettleState.REQUESTED);
                        return projectionFactory.createProjection(Payments.class, c);
                    }).collect(Collectors.toList()));
        });
        return payments;
    }

    public List<Task> fetchTasksForPayment(Long userId) {
        return taskDao.fetchTasks(userId, TaskState.COMPLETED);
    }

    @Override
    public List<Payments> fetchPaymentsForUser(Long userId, SettleState settleState) {
        List<Task> tasks = fetchTasksForPayment(userId);
        List<Payments> payments = new ArrayList<>();
        tasks.stream().forEach(a -> {
            payments.addAll(a.getPayments().stream().filter(b -> b.getSettleState().equals(settleState))
                    .map(c -> projectionFactory.createProjection(Payments.class, c)).collect(Collectors.toList()));
        });
        return payments;
    }

    @Override
    public Double fetchUserOwedAmount(Long userId) {
        return this.fetchPaymentsForUser(userId, SettleState.PAYMENT_RECEIVED).stream().mapToDouble(a -> a.getAmount()).sum();
    }

    @Override
    public List<Payments> settle(Long userId, SettleState settleState) {
        return paymentService.fetchSettleRequests(userId, settleState);
    }

    @Override
    public Users fetchProfileDetails(Long userId) {
        User user = userService.fetchUser(userId);
        user.setAmountOwed(Math.round(this.fetchUserOwedAmount(userId) * 100) / 100D);
        return projectionFactory.createProjection(Users.class, user);
    }

    @Override
    public List<Reasons> fetchReasons(Task task) {
        List<TaskReason> taskReasons = task.getTaskReasons();
        return (!CollectionUtils.isEmpty(taskReasons))
                ? taskReasons
                .stream()
                .map(a -> projectionFactory.createProjection(Reasons.class, a.getReason()))
                .collect(Collectors.toList()) : null;
    }

    @Override
    @Transactional
    public List<Payments> settleAmountForProvider(Long adminId, Long userId) {
        return paymentService.settlePayments(adminId, paymentService.fetchPaymentSettlementsForProvider(userId), new ArrayList<>(), true);
    }

    @Override
    @Transactional
    public StringBuilder createTaskFromAnotherTask(TaskAssignDto dto, Long userId) {
        Task oldTask = taskDao.findById(dto.getTaskId()).orElseThrow(() ->
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_TASK_ID_MESSAGE.toString());
        });
        Task task = new Task();
        task.setIsPrepaid(false);
        task.setPatientId(oldTask.getPatientId());
        task.setType(oldTask.getType());
        task.setPatientName(oldTask.getPatientName());
        task.setPatientPhone(oldTask.getPatientPhone());
        task.setSource(oldTask.getSource());
        task.setCancellationRequested(false);
        task.setState(TaskState.OPEN);
        task.setRefId(dto.getRefId());
        task.setDestinationAddress(addressService.createOrFetchAddress(dto, oldTask.getDestinationAddress().getId()));
        task.setIsActive(true);
        task.setDiagnosticOrderId(dto.getDiagnosticOrderId());
        task.setExpectedArrivalTime((dto.getExpectedArrivalTime()) == null ?
                new Timestamp(System.currentTimeMillis() + (12 * 60 * 60 * 1000)) : dto.getExpectedArrivalTime());
        task.setEndTime(dto.getEndTime());
        task.setStartTime(dto.getStartTime());
        task.setSourceAddress(addressService.fetchSourceAddress());
        if (dto.getPaymentMode() == null || dto.getPaymentMode().isEmpty()) dto.setPaymentMode("");
        if (userId != null) task.setActiveUserId(userId);
        task = taskDao.save(task);
        taskStateFlowService.createFlow(task, TaskStateFlowState.OPEN, null);
        paymentService.create(task, dto);
        return SUCCESS_MESSAGE;
    }

}
