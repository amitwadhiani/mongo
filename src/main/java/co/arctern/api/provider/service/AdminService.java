package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.ServiceType;
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
     * @param areaIds
     * @return
     */
    PaginatedResponse fetchProvidersByArea(List<Long> areaIds, Pageable pageable);

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
    Page<TasksForProvider> fetchTasksByOffering(ServiceType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch tasks through areaIds and time range.
     *
     * @param areaIds
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable);

}
