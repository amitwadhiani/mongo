package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface RiderService extends MessageUtil {

    /**
     * fetch all tasks for rider.
     * @param userId
     * @return
     */
    TasksForRiderResponse fetchTasksForRider(Long userId);

    /**
     * fetch completed tasks for rider.
     * @param userId
     * @return
     */
    List<TasksForRider> fetchCompletedTasksForRider(Long userId);

    /**
     * fetch assigned tasks for rider.
     * @param userId
     * @return
     */
    List<TasksForRider> fetchAssignedTasksForRider(Long userId);

}
