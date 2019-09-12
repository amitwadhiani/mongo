package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.dto.response.HomePageResponseForAdmin;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserService;
import co.arctern.api.provider.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    TaskService taskService;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    UserService userService;

    @Override
    public HomePageResponse fetchHomePage(Long userId, Timestamp start, Timestamp end) {
        HomePageResponse response = new HomePageResponse();
        Integer assignedTasksCount = taskService.fetchUpcomingTasksForUser(userId, TaskState.ASSIGNED, start).size();
        Integer startedTasksCount = taskService.fetchUpcomingTasksForUser(userId, TaskState.STARTED, start).size();
        response.setAssignedTasks(taskService.fetchUpcomingTasksForUser(userId, TaskState.ASSIGNED, start));
        response.setCompletedTasksCount(taskService.fetchTasksForUser(userId, TaskState.COMPLETED, start, end).size());
        response.setAssignedTasksCount(assignedTasksCount);
        response.setStartedTasksCount(startedTasksCount);
        response.setAllTasksCount(assignedTasksCount + startedTasksCount);
        return response;
    }

    @Override
    public HomePageResponseForAdmin fetchHomePageForAdmin(TaskState[] states, Timestamp start, Timestamp end, Pageable pageable) {
        HomePageResponseForAdmin adminResponse = new HomePageResponseForAdmin();
        adminResponse.setTasks(userTaskService.fetchTasks(states, start, end, pageable));
        return adminResponse;
    }
}
