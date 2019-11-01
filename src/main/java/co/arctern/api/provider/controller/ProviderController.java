package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.TasksForProviderResponse;
import co.arctern.api.provider.service.ProviderService;
import co.arctern.api.provider.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

/**
 * apis for provider's tasks.
 */
@BasePathAwareController
@RequestMapping("/provider")
public class ProviderController {

    private final ProviderService providerService;
    private final TokenService tokenService;

    @Autowired
    public ProviderController(ProviderService providerService,
                              TokenService tokenService) {
        this.providerService = providerService;
        this.tokenService = tokenService;
    }

    /**
     * fetch all tasks for a provider api.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/all")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TasksForProviderResponse> fetchTasksForProvider(@RequestParam(value = "userId", required = false) Long userId,
                                                                          Pageable pageable) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(providerService.fetchTasksForProvider(userId, pageable));
    }

    /**
     * fetch assigned tasks for a provider api.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/fetch/assigned")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<PaginatedResponse> fetchAssignedTasksForProvider(@RequestParam(value = "userId", required = false) Long userId,
                                                                           Pageable pageable) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(providerService.fetchAssignedTasksForProvider(userId, pageable));
    }

    /**
     * fetch completed tasks for a provider api.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/fetch/completed")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<PaginatedResponse> fetchCompletedTasksForProvider(@RequestParam(value = "userId", required = false) Long userId,
                                                                            Pageable pageable) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(providerService.fetchCompletedTasksForProvider(userId, pageable));
    }

    /**
     * fetch count of tasks (by state) for a provider api.
     *
     * @param userId
     * @param state
     * @param start
     * @param end
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/count")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Long> fetchCountOfTasksForProvider(@RequestParam(value = "userId", required = false) Long userId,
                                                             @RequestParam("state") TaskState state,
                                                             @RequestParam("start") Timestamp start,
                                                             @RequestParam("end") Timestamp end) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(providerService.fetchCountOfTasksForProvider(userId, state, start, end));
    }

    /**
     * fetch filtered tasks for a provider api.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/filter")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<PaginatedResponse> fetchFilteredTasksForProvider(@RequestParam(value = "userId", required = false) Long userId,
                                                                           @RequestParam("states") TaskState[] states,
                                                                           @RequestParam(value = "start", required = false) Timestamp start,
                                                                           @RequestParam(value = "type", required = false, defaultValue = "SAMPLE_PICKUP") TaskType type,
                                                                           @RequestParam(value = "end", required = false) Timestamp end,
                                                                           Pageable pageable) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(providerService.fetchFilteredTasksForProvider(userId, states, start, end, type, pageable));
    }

}
