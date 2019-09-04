package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.service.RiderService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    TaskService taskService;

    @Transactional
    public TasksForRiderResponse fetchTasksForRider(Long userId,Pageable pageable) {
        TasksForRiderResponse response = new TasksForRiderResponse();
        response.setCompletedTasks(taskService.fetchCompletedTasksForUser(userId,pageable));
        response.setAssignedTasks(taskService.fetchAssignedTasksForUser(userId,pageable));
        return response;
    }

    public PaginatedResponse fetchCompletedTasksForRider(Long userId, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskService.fetchCompletedTasksForUser(userId,pageable), pageable);
    }

    public PaginatedResponse fetchAssignedTasksForRider(Long userId, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(taskService.fetchAssignedTasksForUser(userId,pageable), pageable);
    }
}
