package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.AdminService;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserService;
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
import java.time.ZonedDateTime;
import java.util.List;

/**
 * apis for admin dashboard.
 */
@BasePathAwareController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    HomePageService homePageService;

    /**
     * view providers for admin's / particular areas.
     */
    @CrossOrigin
    @GetMapping("/provider/by-area")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> fetchProvidersByArea(@RequestParam("areaIds") List<Long> areaIds, Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchProvidersByArea(areaIds, pageable));
    }


    /**
     * view tasks for admin's / particular areas.
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
     * view providers for admin's / particular offerings.
     */
    @CrossOrigin
    @GetMapping("/provider/by-offering")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> fetchProvidersByOffering(@RequestParam("offeringIds") List<Long> offeringIds, Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchProvidersByOffering(offeringIds, pageable));
    }

    /**
     * view tasks for admin's / particular offerings.
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
     * mark user active/inactive
     */
    @CrossOrigin
    @GetMapping("/user/state")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> markUserStatus(@RequestParam("state") Boolean state,
                                                        @RequestParam("userId") Long userId) {
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

    @CrossOrigin
    @GetMapping("/home")
    @PreAuthorize(("hasAuthority('ROLE_ADMIN')"))
    public ResponseEntity<?> fetchHomepage(@RequestParam(value = "states", required = false) TaskState[] states,
                                           @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
                                           @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end,
                                           Pageable pageable) {
        if (start == null) start = ZonedDateTime.now();
        if (end == null) end = start.plusDays(2);
        return ResponseEntity.ok(homePageService.fetchHomePageForAdmin(states, DateUtil.zonedDateTimeToTimestampConversion(start), DateUtil.zonedDateTimeToTimestampConversion(end), pageable));
    }

}
