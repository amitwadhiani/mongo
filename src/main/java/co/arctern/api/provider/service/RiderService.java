package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface RiderService extends MessageUtil {

    TasksForRiderResponse fetchTasksForRider(Long userId);

    List<TasksForRider> fetchCompletedTasksForRider(Long userId);

    List<TasksForRider> fetchAssignedTasksForRider(Long userId);

}
