package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.ServiceType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    UserAreaService userAreaService;

    @Autowired
    TaskService taskService;

    @Override
    public PaginatedResponse fetchProvidersByArea(List<Long> areaIds, Pageable pageable) {
        return userAreaService.fetchUsersByArea(areaIds, pageable);
    }

    /**
     * fetch tasks through areaIds
     *
     * @param areaIds
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @Override
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable) {
        return (start == null && end == null) ? taskService.fetchTasksByArea(areaIds, pageable)
                : taskService.fetchTasksByArea(areaIds, start, end, pageable);
    }

    /**
     * fetch providers through offeringIds
     *
     * @param offeringIds
     * @param pageable
     * @return
     */
    @Override
    public PaginatedResponse fetchProvidersByOffering(List<Long> offeringIds, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userService.fetchUsersByOffering(offeringIds, pageable), pageable);
    }

    /**
     * fetch tasks through offeringIds
     *
     * @param type
     * @param pageable
     * @return
     */
    @Override
    public Page<TasksForProvider> fetchTasksByOffering(ServiceType type, Timestamp start, Timestamp end, Pageable pageable) {
        return (start == null && end == null) ? taskService.fetchTasksByType(type, pageable)
                : taskService.fetchTasksByType(type, start, end, pageable);
    }

}
