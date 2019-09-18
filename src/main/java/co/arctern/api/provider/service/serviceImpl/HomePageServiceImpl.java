package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.dto.response.HomePageResponseForAdmin;
import co.arctern.api.provider.dto.response.PaginatedResponse;
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
    public HomePageResponseForAdmin fetchHomePageForAdmin(TaskState[] states, Timestamp start, Timestamp end,
                                                          List<Long> areaIds, TaskType taskType, String patientFilterValue,
                                                          Pageable pageable) {
        HomePageResponseForAdmin adminResponse = new HomePageResponseForAdmin();
        PaginatedResponse allTasks = userTaskService.fetchTasks(states, start, end, areaIds, taskType, patientFilterValue, pageable);
        adminResponse.setTasks(allTasks);
        adminResponse.setTasksCount(allTasks.getTotalElements());
        PaginatedResponse cancelledTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.CANCELLED}, start, end, areaIds, taskType, patientFilterValue, pageable);
        adminResponse.setCancelledTasks(cancelledTasks);
        adminResponse.setTasksCount(cancelledTasks.getTotalElements());
        PaginatedResponse completedTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.COMPLETED}, start, end, areaIds, taskType, patientFilterValue, pageable);
        adminResponse.setCompletedTasks(completedTasks);
        adminResponse.setTasksCount(completedTasks.getTotalElements());
        PaginatedResponse openTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.OPEN}, start, end, areaIds, taskType, patientFilterValue, pageable);
        adminResponse.setOpenTasks(openTasks);
        adminResponse.setTasksCount(openTasks.getTotalElements());
        PaginatedResponse pendingTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.ASSIGNED}, start, end, areaIds, taskType, patientFilterValue, pageable);
        adminResponse.setPendingTasks(pendingTasks);
        adminResponse.setTasksCount(pendingTasks.getTotalElements());
        PaginatedResponse startedTasks = userTaskService.fetchTasks(new TaskState[]{TaskState.STARTED}, start, end, areaIds, taskType, patientFilterValue, pageable);
        adminResponse.setStartedTasks(startedTasks);
        adminResponse.setTasksCount(startedTasks.getTotalElements());
        return adminResponse;
    }
}
