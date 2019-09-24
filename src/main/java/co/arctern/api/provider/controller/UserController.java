package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * apis for user creation.
 */
@BasePathAwareController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * to create new user api-> allowed for Admin only .
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> createNewUser(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    /**
     * fetch all users api.
     *
     * @return
     */
    @GetMapping("/fetch/all")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> fetchAll(Pageable pageable) {
        return ResponseEntity.ok(userService.fetchAll(pageable));
    }


}
