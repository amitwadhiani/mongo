package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.service.TokenService;
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

    private final HomePageService homePageService;
    private final TokenService tokenService;

    @Autowired
    public HomePageController(HomePageService homePageService,
                              TokenService tokenService) {
        this.homePageService = homePageService;
        this.tokenService = tokenService;
    }

    /**
     * fetch tasks filtered by user and time range api.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/tasks")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<HomePageResponse> fetchHomePage(@RequestParam(value = "userId", required = false) Long userId) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(homePageService.fetchHomePage(userId));
    }
}
