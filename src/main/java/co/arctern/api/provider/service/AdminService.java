package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface AdminService extends MessageUtil {

    /**
     * fetch providers through areaIds
     *
     * @param clusterIds
     * @return
     */
    PaginatedResponse fetchProvidersByArea(List<Long> clusterIds, Pageable pageable);

    /**
     * fetch providers through offeringIds
     *
     * @param offeringIds
     * @return
     */
    PaginatedResponse fetchProvidersByOffering(List<Long> offeringIds, Pageable pageable);

    /**
     * fetch tasks through type and time range.
     *
     * @param type
     * @param start
     * @param end
     * @return
     */
    Page<TasksForProvider> fetchTasksByOffering(OfferingType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks through areaIds and time range.
     *
     * @param ids
     * @param value
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<TasksForProvider> fetchTasksAndFilter(List<Long> ids, TaskType taskType, String value, Timestamp start, Timestamp end, Pageable pageable);

    }
