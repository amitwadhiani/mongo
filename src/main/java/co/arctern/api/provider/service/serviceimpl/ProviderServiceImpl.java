package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.TasksForProviderResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.ProviderService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserTaskService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final TaskService taskService;
    private final ProjectionFactory projectionFactory;
    private final UserTaskService userTaskService;

    @Autowired
    public ProviderServiceImpl(TaskService taskService,
                               ProjectionFactory projectionFactory,
                               UserTaskService userTaskService) {
        this.taskService = taskService;
        this.projectionFactory = projectionFactory;
        this.userTaskService = userTaskService;
    }

    @Transactional
    @Override
    public TasksForProviderResponse fetchTasksForProvider(Long userId, Pageable pageable) {
        TasksForProviderResponse response = new TasksForProviderResponse();
        response.setCompletedTasks(taskService.fetchCompletedTasksForUser(userId, pageable));
        response.setAssignedTasks(taskService.fetchAssignedTasksForUser(userId, pageable));
        return response;
    }

    @Override
    public PaginatedResponse fetchCompletedTasksForProvider(Long userId, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskService.fetchCompletedTasksForUser(userId, pageable), pageable);
    }

    @Override
    public PaginatedResponse fetchAssignedTasksForProvider(Long userId, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskService.fetchAssignedTasksForUser(userId, pageable), pageable);
    }

    @Override
    public Long fetchCountOfTasksForProvider(Long userId, TaskState state, Timestamp start, Timestamp end) {
        return userTaskService.countByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(
                userId, state, start, end);
    }

    @Override
    public PaginatedResponse fetchFilteredTasksForProvider(Long userId, TaskState[] states, Timestamp start, Timestamp end, TaskType taskType, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody((start == null && end == null) ? userTaskService.fetchTasksForUser(userId, states, pageable)
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask())) :
                userTaskService.fetchTasksForUser(userId, states, taskType, start, end, pageable)
                        .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask())), pageable);
    }

}
