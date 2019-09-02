package co.arctern.api.provider.dto.response;

import co.arctern.api.provider.dto.response.projection.TasksForRider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TasksForRiderResponse {

    public List<TasksForRider> completedTasks;
    public List<TasksForRider> assignedTasks;
    public List<TasksForRider> cancelledTasks;

}
