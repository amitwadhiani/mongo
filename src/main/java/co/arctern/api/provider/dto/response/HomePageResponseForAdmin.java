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
    PaginatedResponse startedTasks;
    PaginatedResponse cancelledTasks;
    Long tasksCount;
    Long openTasksCount;
    Long completedTasksCount;
    Long pendingTasksCount;
    Long startedTasksCount;
    Long cancelledTasksCount;

}
