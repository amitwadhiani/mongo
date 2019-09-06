package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.service.ProviderService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    TaskService taskService;

    @Transactional
    @Override
    public TasksForRiderResponse fetchTasksForRider(Long userId,Pageable pageable) {
        TasksForRiderResponse response = new TasksForRiderResponse();
        response.setCompletedTasks(taskService.fetchCompletedTasksForUser(userId,pageable));
        response.setAssignedTasks(taskService.fetchAssignedTasksForUser(userId,pageable));
        return response;
    }

    @Override
    public PaginatedResponse fetchCompletedTasksForRider(Long userId, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskService.fetchCompletedTasksForUser(userId,pageable), pageable);
    }

    @Override
    public PaginatedResponse fetchAssignedTasksForRider(Long userId, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskService.fetchAssignedTasksForUser(userId,pageable), pageable);
    }
}
