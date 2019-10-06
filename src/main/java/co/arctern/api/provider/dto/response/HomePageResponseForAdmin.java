package co.arctern.api.provider.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response for admin homepage.
 */
@Data
@NoArgsConstructor
public class HomePageResponseForAdmin {

    private PaginatedResponse tasks;
    private PaginatedResponse openTasks;
    private PaginatedResponse completedTasks;
    private PaginatedResponse pendingTasks;
    private PaginatedResponse acceptedTasks;
    private PaginatedResponse startedTasks;
    private PaginatedResponse cancelledTasks;
    private PaginatedResponse cancelRequests;
    private Long tasksCount;
    private Long openTasksCount;
    private Long acceptedTasksCount;
    private Long completedTasksCount;
    private Long pendingTasksCount;
    private Long cancelRequestsCount;
    private Long startedTasksCount;
    private Long cancelledTasksCount;

}
