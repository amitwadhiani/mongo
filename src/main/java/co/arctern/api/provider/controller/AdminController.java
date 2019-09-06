package co.arctern.api.provider.controller;

import co.arctern.api.provider.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<?> fetchProvidersByArea(@RequestParam("areaIds") List<Long> areaIds, Pageable pageable) {
        return ResponseEntity.ok(adminService.fetchProvidersByArea(areaIds, pageable));
    }

}
