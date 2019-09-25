package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * apis for admin dashboard.
 */
@BasePathAwareController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final TaskService taskService;
    private final TokenService tokenService;
    private final HomePageService homePageService;

    @Autowired
    public AdminController(AdminService adminService,
                           UserService userService,
                           TaskService taskService,
                           TokenService tokenService,
                           HomePageService homePageService) {
        this.adminService = adminService;
        this.userService = userService;
        this.taskService = taskService;
        this.tokenService = tokenService;
        this.homePageService = homePageService;
    }

    /**
     * view providers for admin's / particular areas api.
     */
    @CrossOrigin
    @GetMapping("/provider/by-area")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> fetchProvidersByArea(@RequestParam("areaIds") List<Long> areaIds, Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchProvidersByArea(areaIds, pageable));
    }


    /**
     * view tasks for admin's / particular areas api.
     */
    @CrossOrigin
    @GetMapping("/task/by-area")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<TasksForProvider>> fetchTasksByArea(@RequestParam("areaIds") List<Long> areaIds,
                                                                   @RequestParam("start") Timestamp start,
                                                                   @RequestParam("end") Timestamp end,
                                                                   Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchTasksByArea(areaIds, start, end, pageable));
    }

    /**
     * view providers for admin's / particular offerings api.
     */
    @CrossOrigin
    @GetMapping("/provider/by-offering")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> fetchProvidersByOffering(@RequestParam("offeringIds") List<Long> offeringIds, Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchProvidersByOffering(offeringIds, pageable));
    }

    /**
     * view tasks for admin's / particular offerings api.
     */
    @CrossOrigin
    @GetMapping("/task/by-offering")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<TasksForProvider>> fetchTasksByOffering(@RequestParam("type") OfferingType type,
                                                                       @RequestParam("start") Timestamp start,
                                                                       @RequestParam("end") Timestamp end,
                                                                       Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchTasksByOffering(type, start, end, pageable));
    }

    /**
     * mark user active/inactive api.
     */
    @CrossOrigin
    @GetMapping("/user/state")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> markUserStatus(@RequestParam("state") Boolean state,
                                                        @RequestParam(value = "userId", required = false) Long userId) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(userService.markUserInactive(userId, state));
    }

    /**
     * cancel requests
     */
    @CrossOrigin
    @GetMapping("/task/cancel-requests")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> seeCancelRequests(Pageable pageable) {
        return ResponseEntity.ok(taskService.seeCancelRequests(pageable));
    }

    /**
     * admin homepage api.
     *
     * @param states
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @CrossOrigin
    @GetMapping("/home")
    @PreAuthorize(("hasAuthority('ROLE_ADMIN')"))
    public ResponseEntity<?> fetchHomepage(@RequestParam(value = "states", required = false) TaskState[] states,
                                           @RequestParam(value = "areaIds", required = false) List<Long> areaIds,
                                           @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
                                           @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end,
                                           @RequestParam(value = "orderId", required = false) Long orderId,
                                           @RequestParam(value = "taskType", required = false, defaultValue = "SAMPLE_PICKUP") TaskType taskType,
                                           @RequestParam(value = "patientFilterValue", required = false) String patientFilterValue,
                                           Pageable pageable) {
        Timestamp startTs = (start != null) ? DateUtil.fetchTodayTimestamp(start) :
                DateUtil.fetchTodayTimestamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Kolkata")));
        Timestamp endTs = (end != null) ? DateUtil.fetchTodayTimestamp(end) : DateUtil.fetchTodayTimestamp(ZonedDateTime.now().plusDays(1));
        return ResponseEntity.ok(homePageService.fetchHomePageForAdmin(states, startTs, endTs,
                areaIds,
                taskType,
                orderId,
                patientFilterValue,
                pageable));
    }

    /**
     * fetch all users
     */
    @CrossOrigin
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> fetchAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.fetchAllUsersByAdmin(pageable));
    }


}
