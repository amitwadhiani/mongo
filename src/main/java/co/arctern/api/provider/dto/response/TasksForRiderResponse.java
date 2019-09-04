package co.arctern.api.provider.dto.response;

import co.arctern.api.provider.dto.response.projection.TasksForRider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * response for tasks of a rider.
 */
@Data
@NoArgsConstructor
public class TasksForRiderResponse {

    public Page<TasksForRider> completedTasks;
    public Page<TasksForRider> assignedTasks;

}
