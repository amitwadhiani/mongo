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

    private List<TasksForProvider> assignedTasks;
    private Integer assignedTasksCount;
    private Integer acceptedTasksCount;
    private Integer completedTasksCount;
    private Integer startedTasksCount;
    private List<TasksForProvider> startedTasks;
    private Integer allTasksCount;
}
