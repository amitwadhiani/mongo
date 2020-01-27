package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.TaskStateFlowState;
import co.arctern.api.provider.dto.request.RescheduleRequestBody;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * apis for handling, assigning, reassigning and cancelling tasks by Admin / Rider.
 */
@BasePathAwareController
@RequestMapping("/task")
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final TokenService tokenService;

    @Autowired
    public TaskController(TaskService taskService, TokenService tokenService) {
        this.taskService = taskService;
        this.tokenService = tokenService;
    }

    /**
     * assign tasks to user ( automatic task creation and assignment for a particular diagnosticOrder ) api.
     *
     * @param dto
     * @return
     */
    @PostMapping("/create-assign")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> assignTaskToUser(@RequestBody TaskAssignDto dto) {
        return ResponseEntity.ok(taskService.createTaskFromAnotherTask(dto, tokenService.fetchUserId()));
    }

    /**
     * re-assign tasks to user api.
     *
     * @param taskId
     * @param userId
     * @return
     */
    @PostMapping("/reassign")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> reassignTaskToUser(@RequestParam("taskId") Long taskId,
                                                            @RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(taskService.reassignTask(taskId, userId));
    }

    /**
     * assign tasks to user api.
     *
     * @param taskIds
     * @param userId
     * @return
     */
    @PostMapping("/assign")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> assignTaskToUser(@RequestParam("taskIds") List<Long> taskIds,
                                                          @RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(taskService.assignTasks(taskIds, userId));
    }

    /**
     * reschedule a task api.
     *
     * @param request
     * @return
     */
    @PostMapping("/reschedule")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> rescheduleTask(@RequestBody RescheduleRequestBody request) {
        Long userId = request.getUserId();
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(taskService.rescheduleTask(request.getTaskId(), null, request.getTime(), request.getStartTime(), request.getEndTime()));
    }

    /**
     * start a task api.
     *
     * @param taskId
     * @param userId
     * @return
     */
    @PostMapping("/start")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StringBuilder> startTaskToUser(@RequestParam("taskId") Long taskId,
                                                         @RequestParam(value = "userId", required = false) Long userId) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(taskService.startTask(taskId, userId));
    }

    /**
     * accept or reject task api.
     *
     * @param state
     * @param taskId
     * @return
     */
    @PostMapping("/accept-reject")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StringBuilder> acceptOrRejectTask(@RequestParam(value = "state", required = false, defaultValue = "ACCEPTED") TaskStateFlowState state,
                                                            @RequestParam(value = "taskId", required = false) Long taskId,
                                                            @RequestParam(value = "reasonIds", required = false) List<Long> reasonIds) {
        return ResponseEntity.ok(taskService.acceptOrRejectAssignedTask(taskId, reasonIds, state));
    }


    /**
     * cancel task / reassign task api.
     *
     * @param isCancelled
     * @param taskId
     * @param userId
     * @return
     */
    @PostMapping("/cancel")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> cancelTask(@RequestParam(value = "isCancelled", defaultValue = "true", required = false) Boolean isCancelled,
                                                    @RequestParam("taskId") Long taskId,
                                                    @RequestParam(value = "userId", required = false) Long userId,
                                                    @RequestParam("reasonIds") List<Long> reasonIds) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(taskService.cancelTask(isCancelled, taskId, userId, reasonIds));
    }

    /**
     * request cancellation for a task by user api.
     *
     * @param cancelRequest
     * @param taskId
     * @return
     */
    @PostMapping("/cancel-request")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StringBuilder> requestToCancelTask(@RequestParam(value = "cancelRequest", defaultValue = "true") Boolean cancelRequest,
                                                             @RequestParam("taskId") Long taskId,
                                                             @RequestParam("reasonIds") List<Long> reasonIds) {
        return ResponseEntity.ok(taskService.requestCancellation(cancelRequest, taskId, reasonIds));
    }

    /**
     * create new task api. (called from order-api)
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TasksForProvider> createTask(@RequestBody TaskAssignDto dto) {
        return ResponseEntity.ok(taskService.fetchProjectedResponseFromPost(dto));
    }

    /**
     * cancel tasks related to an order api. (called from order-api)
     *
     * @param taskIds
     * @param userId
     * @return
     */
    @PostMapping("/cancel/all")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StringBuilder> cancelAllTasks(@RequestParam("taskIds") List<Long> taskIds,
                                                        @RequestParam(value = "userId", required = false) Long userId) {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(taskService.cancelAllTasks(taskIds, userId));
    }

}
