package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.service.RiderService;
import co.arctern.api.provider.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    TaskService taskService;

    @Transactional
    public TasksForRiderResponse fetchTasksForRider(Long userId) {
        TasksForRiderResponse response = new TasksForRiderResponse();
        response.setCompletedTasks(taskService.fetchCompletedTasksForUser(userId));
        response.setAssignedTasks(taskService.fetchAssignedTasksForUser(userId));
        response.setCancelledTasks(taskService.fetchCancelledTasksForUser(userId));
        return response;
    }

    public List<TasksForRider> fetchCompletedTasksForRider(Long userId) {
        return taskService.fetchCompletedTasksForUser(userId);
    }

    public List<TasksForRider> fetchAssignedTasksForRider(Long userId) {
        return taskService.fetchAssignedTasksForUser(userId);
    }
}
