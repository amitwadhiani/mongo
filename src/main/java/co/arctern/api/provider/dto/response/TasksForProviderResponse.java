package co.arctern.api.provider.dto.response;

import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * response for tasks of a rider.
 */
@Data
@NoArgsConstructor
public class TasksForProviderResponse {

    private Page<TasksForProvider> completedTasks;
    private Page<TasksForProvider> assignedTasks;

}
