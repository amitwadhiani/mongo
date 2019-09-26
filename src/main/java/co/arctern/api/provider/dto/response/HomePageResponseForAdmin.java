package co.arctern.api.provider.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response for admin homepage.
 */
@Data
@NoArgsConstructor
public class HomePageResponseForAdmin {

    PaginatedResponse tasks;
    PaginatedResponse openTasks;
    PaginatedResponse completedTasks;
    PaginatedResponse pendingTasks;
    PaginatedResponse acceptedTasks;
    PaginatedResponse startedTasks;
    PaginatedResponse cancelledTasks;
    PaginatedResponse cancelRequests;
    Long tasksCount;
    Long openTasksCount;
    Long acceptedTasksCount;
    Long completedTasksCount;
    Long pendingTasksCount;
    Long cancelRequestsCount;
    Long startedTasksCount;
    Long cancelledTasksCount;

}