package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.dto.response.HomePageResponseForAdmin;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class HomePageServiceImpl implements HomePageService {

    private final TaskService taskService;

    @Autowired
    public HomePageServiceImpl(TaskService taskService) {
        this.taskService = taskService;
    }

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
    public HomePageResponseForAdmin fetchHomePageForAdmin(TaskState[] states,
                                                          Timestamp start, Timestamp end,
                                                          List<Long> areaIds, TaskType taskType, Long orderId,
                                                          String patientFilterValue,
                                                          Long providerId,
                                                          Pageable pageable) {
        HomePageResponseForAdmin adminResponse = new HomePageResponseForAdmin();
        PaginatedResponse allTasks = taskService.fetchTasks(states, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable);
        return this.setCount(adminResponse,
                allTasks,
                taskService.fetchTasks(new TaskState[]{TaskState.STARTED}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable),
                taskService.fetchTasks(new TaskState[]{TaskState.ACCEPTED}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable),
                taskService.fetchTasks(new TaskState[]{TaskState.REJECTED}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable),
                taskService.fetchTasks(new TaskState[]{TaskState.RESCHEDULED}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable),
                taskService.fetchTasks(new TaskState[]{TaskState.OPEN}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable),
                taskService.fetchTasks(new TaskState[]{TaskState.ASSIGNED}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable),
                taskService.fetchTasks(new TaskState[]{TaskState.CANCELLED}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable),
                taskService.fetchTasks(new TaskState[]{TaskState.COMPLETED}, start, end, areaIds, taskType, orderId, patientFilterValue, providerId, pageable)
        );
    }

    @Override
    public HomePageResponseForAdmin setCount(HomePageResponseForAdmin adminResponse, PaginatedResponse allTasks, PaginatedResponse startedTasks, PaginatedResponse acceptedTasks, PaginatedResponse rejectedTasks, PaginatedResponse rescheduledTasks,
                                             PaginatedResponse openTasks, PaginatedResponse pendingTasks,
                                             PaginatedResponse cancelledTasks,
                                             PaginatedResponse completedTasks) {
        adminResponse.setTasks(allTasks);
        adminResponse.setAcceptedTasksCount(acceptedTasks.getTotalElements());
        adminResponse.setRejectedTasksCount(rejectedTasks.getTotalElements());
        adminResponse.setRescheduledTasksCount(rescheduledTasks.getTotalElements());
        adminResponse.setStartedTasksCount(startedTasks.getTotalElements());
        adminResponse.setOpenTasksCount(openTasks.getTotalElements());
        adminResponse.setPendingTasksCount(pendingTasks.getTotalElements());
        adminResponse.setCancelledTasksCount(cancelledTasks.getTotalElements());
        adminResponse.setCompletedTasksCount(completedTasks.getTotalElements());
        return adminResponse;
    }
}
