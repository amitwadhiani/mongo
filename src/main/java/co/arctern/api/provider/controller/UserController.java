package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.service.UserService;
import org.checkerframework.checker.signature.qual.ClassGetName;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    /**
     * to create new user -> allowed for Admin only.
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
    public ResponseEntity<StringBuilder> createNewUser(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @GetMapping("/fetch/all")
    @CrossOrigin
    public ResponseEntity<List<Users>> fetchAll() {
        return ResponseEntity.ok(userService.fetchAll());
    }

}
