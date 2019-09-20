package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.dto.response.HomePageResponseForAdmin;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserService;
import co.arctern.api.provider.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    TaskService taskService;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    UserService userService;

    @Override
    public HomePageResponse fetchHomePage(Long userId) {
        HomePageResponse response = new HomePageResponse();
        List<TasksForProvider> assignedTasks = taskService.fetchTasksForUser(userId, TaskState.ASSIGNED);
        Integer assignedTasksCount = assignedTasks.size();
        List<TasksForProvider> startedTasks = taskService.fetchTasksForUser(userId, TaskState.STARTED);
        Integer acceptedTasksCount = taskService.fetchTasksForUser(userId, TaskState.ACCEPTED).size();
        Integer startedTasksCount = startedTasks.size();
        response.setAssignedTasks(assignedTasks);
        response.setStartedTasks(startedTasks);
        response.setCompletedTasksCount(taskService.fetchTasksForUser(userId, TaskState.COMPLETED).size());
        response.setAssignedTasksCount(assignedTasksCount);
        response.setAcceptedTasksCount(acceptedTasksCount);
        response.setStartedTasksCount(startedTasksCount);
        response.setAllTasksCount(assignedTasksCount + startedTasksCount + acceptedTasksCount);
        return response;
    }

    @Override
    public HomePageResponseForAdmin fetchHomePageForAdmin(TaskState[] states, Timestamp start, Timestamp end,
                                                          List<Long> areaIds, TaskType taskType, Long orderId,
                                                          String patientFilterValue,
                                                          Pageable pageable) {
        HomePageResponseForAdmin adminResponse = new HomePageResponseForAdmin();
        PaginatedResponse allTasks = userTaskService.fetchTasks(states, start, end, areaIds, taskType, orderId, patientFilterValue, pageable);
        adminResponse.setTasks(allTasks);
        adminResponse.setTasksCount(allTasks.getTotalElements());
        PaginatedResponse cancelledTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.CANCELLED}, start, end, areaIds, taskType, orderId, patientFilterValue, pageable);
        adminResponse.setCancelledTasks(cancelledTasks);
        adminResponse.setCancelledTasksCount(cancelledTasks.getTotalElements());
        PaginatedResponse completedTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.COMPLETED}, start, end, areaIds, taskType, orderId, patientFilterValue, pageable);
        adminResponse.setCompletedTasks(completedTasks);
        adminResponse.setCompletedTasksCount(completedTasks.getTotalElements());
        PaginatedResponse openTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.OPEN}, start, end, areaIds, taskType, orderId, patientFilterValue, pageable);
        adminResponse.setOpenTasks(openTasks);
        adminResponse.setOpenTasksCount(openTasks.getTotalElements());
        PaginatedResponse pendingTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.ASSIGNED}, start, end, areaIds, taskType, orderId, patientFilterValue, pageable);
        adminResponse.setPendingTasks(pendingTasks);
        adminResponse.setPendingTasksCount(pendingTasks.getTotalElements());
        PaginatedResponse startedTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.STARTED}, start, end, areaIds, taskType, orderId, patientFilterValue, pageable);
        adminResponse.setStartedTasks(startedTasks);
        adminResponse.setStartedTasksCount(startedTasks.getTotalElements());
        return adminResponse;
    }
}
