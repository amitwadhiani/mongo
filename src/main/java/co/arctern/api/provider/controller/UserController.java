package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dto.request.ProviderRequestForOrderItemDto;
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

import java.util.List;

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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<Users> fetchDetails(@RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok(userService.fetchDetails(taskId));
    }

    /**
     * search user api.
     *
     * @param value
     * @param pageable
     * @return
     */
    @CrossOrigin
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<Page<Users>> search(@RequestParam("value") String value, Pageable pageable) {
        return ResponseEntity.ok(userService.search(value, pageable));
    }

    /**
     * fetch user profile api.
     *
     * @return
     */
    @GetMapping("/profile")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<Users> fetchProfileDetails() {
        return ResponseEntity.ok(taskService.fetchProfileDetails(tokenService.fetchUserId()));
    }

    /**
     * activate/deactivate user api.
     *
     * @param userId
     * @param isActive
     * @return
     */
    @PatchMapping("/activate")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> activateOrDeactivateUser(@RequestParam("userId") Long userId,
                                                                  @RequestParam("isActive") Boolean isActive) {
        return ResponseEntity.ok(userService.activateOrDeactivateUser(userId, isActive));
    }

    /**
     * fetch user by pinCode api.
     *
     * @param pinCode
     * @return
     */
    @GetMapping("/fetch/by-pincode")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> fetchUserByPincode(@RequestParam("pinCode") String pinCode) {
        return ResponseEntity.ok(userService.fetchUserByPincode(pinCode));
    }

    /**
     * fetch users by task ids ( order-items ).
     *
     * @param dtos
     * @return
     */
    @PostMapping("/fetch/by-task")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProviderRequestForOrderItemDto>> fetchUserByTaskId(@RequestBody List<ProviderRequestForOrderItemDto> dtos) {
        return ResponseEntity.ok(userService.fetchUserByTaskId(dtos));
    }

    /**
     * one time call to replace area-user mapping with user-cluster (structure changes).
     * WARNING: only for one time use
     *
     * @return
     */
    @PostMapping("/post/by-pincode")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StringBuilder> replaceAreasWithClusters() {
        return ResponseEntity.ok(userService.replaceAreasWithClusters());
    }
}
