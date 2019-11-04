package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.TasksForProviderResponse;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;

public interface ProviderService extends MessageUtil {

    /**
     * fetch all tasks for provider.
     *
     * @param userId
     * @return
     */
    TasksForProviderResponse fetchTasksForProvider(Long userId, Pageable pageable);

    /**
     * fetch completed tasks for provider.
     *
     * @param userId
     * @return
     */
    PaginatedResponse fetchCompletedTasksForProvider(Long userId, Pageable pageable);

    /**
     * fetch assigned tasks for provider.
     *
     * @param userId
     * @return
     */
    PaginatedResponse fetchAssignedTasksForProvider(Long userId, Pageable pageable);

    /**
     * fetch filtered tasks for provider.
     *
     * @param userId
     * @return
     */
    PaginatedResponse fetchFilteredTasksForProvider(Long userId, TaskState[] states, Timestamp start, Timestamp end, TaskType taskType, Pageable pageable);

    /**
     * fetch count of tasks by state for provider.
     *
     * @param userId
     * @return
     */
    Long fetchCountOfTasksForProvider(Long userId, TaskState state, Timestamp start, Timestamp end);

}
