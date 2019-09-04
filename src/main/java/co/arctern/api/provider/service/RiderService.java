package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiderService extends MessageUtil {

    /**
     * fetch all tasks for rider.
     *
     * @param userId
     * @return
     */
    TasksForRiderResponse fetchTasksForRider(Long userId);

    /**
     * fetch completed tasks for rider.
     *
     * @param userId
     * @return
     */
    PaginatedResponse fetchCompletedTasksForRider(Long userId, Pageable pageable);

    /**
     * fetch assigned tasks for rider.
     *
     * @param userId
     * @return
     */
    PaginatedResponse fetchAssignedTasksForRider(Long userId, Pageable pageable);

}
