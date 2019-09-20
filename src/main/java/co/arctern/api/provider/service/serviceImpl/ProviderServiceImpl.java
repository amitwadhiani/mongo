package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
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

    @Autowired
    TaskService taskService;

    @Autowired
    ProjectionFactory projectionFactory;

    @Autowired
    UserTaskService userTaskService;

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
    public PaginatedResponse fetchFilteredTasksForProvider(Long userId, TaskState state, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userTaskService.fetchTasksForUser(userId, state, pageable)
                .map(a -> projectionFactory.createProjection(TasksForProvider.class, a.getTask())), pageable);
    }

}
