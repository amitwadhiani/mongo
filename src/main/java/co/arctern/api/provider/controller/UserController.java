package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TokenService;
import co.arctern.api.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * apis for user creation.
 */
@BasePathAwareController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService,
                          TokenService tokenService,
                          TaskService taskService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.taskService = taskService;
    }

    /**
     * to create new user api-> allowed for Admin only .
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> createNewUser(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    /**
     * to update details in user's profile ( excluding password, email and username) .
     *
     * @param dto
     * @return
     */
    @PatchMapping("/update")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> updateUser(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.updateUser(dto));
    }

    /**
     * fetch all users api.
     *
     * @return
     */
    @GetMapping("/fetch/all")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PaginatedResponse> fetchAll(@RequestParam(value = "taskType", required = false) TaskType taskType,
                                                      @RequestParam(value = "clusterId", required = false) Long clusterId
            , Pageable pageable) {
        return ResponseEntity.ok((clusterId != null) ?
                ((taskType == null)
                        ? userService.fetchAllByCluster(clusterId, pageable)
                        : userService.fetchAllByTaskTypeAndCluster(taskType, clusterId, pageable))
                : ((taskType == null)
                ? userService.fetchAll(pageable)
                : userService.fetchAllByTaskType(taskType, pageable))
        );
    }

    /**
     * fetch task details from taskId api.
     *
     * @param taskId
     * @return
     */
    @GetMapping("/task/detail")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Users> fetchDetails(@RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok(userService.fetchDetails(taskId));
    }

    @CrossOrigin
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<Users>> search(@RequestParam("value") String value, Pageable pageable) {
        return ResponseEntity.ok(userService.search(value, pageable));
    }

    @GetMapping("/profile")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Users> fetchProfileDetails() {
        return ResponseEntity.ok(taskService.fetchProfileDetails(tokenService.fetchUserId()));
    }

}
