package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.AdminService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserAreaService;
import co.arctern.api.provider.service.UserService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final UserAreaService userAreaService;
    private final TaskService taskService;

    @Autowired
    public AdminServiceImpl(UserService userService,
                            UserAreaService userAreaService,
                            TaskService taskService) {
        this.userService = userService;
        this.userAreaService = userAreaService;
        this.taskService = taskService;
    }

    @Override
    public PaginatedResponse fetchProvidersByArea(List<Long> areaIds, Pageable pageable) {
        return userAreaService.fetchUsersByArea(areaIds, pageable);
    }

    @Override
    public Page<TasksForProvider> fetchTasksByArea(List<Long> areaIds, Timestamp start, Timestamp end, Pageable pageable) {
        return (start == null && end == null) ? taskService.fetchTasksByArea(areaIds, pageable)
                : taskService.fetchTasksByArea(areaIds, start, end, pageable);
    }

    @Override
    public PaginatedResponse fetchProvidersByOffering(List<Long> offeringIds, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userService.fetchUsersByOffering(offeringIds, pageable), pageable);
    }

   @Override
    public Page<TasksForProvider> fetchTasksByOffering(OfferingType type, Timestamp start, Timestamp end, Pageable pageable) {
        return (start == null && end == null) ? taskService.fetchTasksByType(type, pageable)
                : taskService.fetchTasksByType(type, start, end, pageable);
    }

}
