package co.arctern.api.provider.dto.response;

import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * response body for app homepage.
 */
@Data
@NoArgsConstructor
public class HomePageResponse {

    List<TasksForProvider> assignedTasks;
    Integer assignedTasksCount;
    Integer acceptedTasksCount;
    Integer completedTasksCount;
    Integer startedTasksCount;
    Integer rejectedTasksCount;
    List<TasksForProvider> startedTasks;
    Integer allTasksCount;
}
