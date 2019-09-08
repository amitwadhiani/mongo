package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.ServiceType;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

/**
 * apis for admin dashboard.
 */
@BasePathAwareController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    /**
     * view providers for admin's / particular areas.
     */
    @CrossOrigin
    @GetMapping("/provider/by-area")
    public ResponseEntity<PaginatedResponse> fetchProvidersByArea(@RequestParam("areaIds") List<Long> areaIds, Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchProvidersByArea(areaIds, pageable));
    }

    /**
     * view tasks for admin's / particular areas.
     */
    @CrossOrigin
    @GetMapping("/task/by-area")
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
    public ResponseEntity<PaginatedResponse> fetchProvidersByOffering(@RequestParam("offeringIds") List<Long> offeringIds, Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchProvidersByOffering(offeringIds, pageable));
    }

    /**
     * view tasks for admin's / particular offerings.
     */
    @CrossOrigin
    @GetMapping("/task/by-offering")
    public ResponseEntity<Page<TasksForProvider>> fetchTasksByOffering(@RequestParam("type") ServiceType type,
                                                                       @RequestParam("start") Timestamp start,
                                                                       @RequestParam("end") Timestamp end,
                                                                       Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchTasksByOffering(type, start, end, pageable));
    }

}
