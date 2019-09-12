package co.arctern.api.provider.dto.response;

import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HomePageResponse {

    List<TasksForProvider> assignedTasks;
    Integer assignedTasksCount;
    Integer completedTasksCount;
    Integer startedTasksCount;
    Integer allTasksCount;
}
