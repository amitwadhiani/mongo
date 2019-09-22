package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * homepage apis for app ONLY.
 */
@BasePathAwareController
@RequestMapping("/home")
public class HomePageController {

    @Autowired
    HomePageService homePageService;

    /**
     * fetch tasks filtered by user and time range api.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/tasks")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<HomePageResponse> fetchHomePage(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(homePageService.fetchHomePage(userId));
    }
}
