package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.dto.request.RescheduleRequestBody;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.TasksForProvider;
import co.arctern.api.provider.service.TaskService;
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

    @Autowired
    TaskService taskService;

    /**
     * assign tasks to user ( automatic task creation and assignment for a particular diagnosticOrder ) api.
     *
     * @param dto
     * @return
     */
    @PostMapping("/create-assign")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<StringBuilder> assignTaskToUser(@RequestBody TaskAssignDto dto) {
        return ResponseEntity.ok(taskService.createTaskAndAssignUser(dto));
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> reassignTaskToUser(@RequestParam("taskId") Long taskId,
                                                            @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(taskService.reassignTask(taskId, userId));
    }

    /**
     * assign tasks to user api.
     *
     * @param taskId
     * @param userId
     * @return
     */
    @PostMapping("/assign")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> assignTaskToUser(@RequestParam("taskId") Long taskId,
                                                          @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(taskService.assignTask(taskId, userId));
    }

    /**
     * reschedule a task api.
     *
     * @param request
     * @return
     */
    @PostMapping("/reschedule")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StringBuilder> rescheduleTask(@RequestBody RescheduleRequestBody request) {
        return ResponseEntity.ok(taskService.rescheduleTask(request.getTaskId(), request.getUserId(), request.getTime()));
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
                                                         @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(taskService.startTask(taskId, userId));
    }

    @PostMapping("/accept-reject")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StringBuilder> acceptOrRejectTask(@RequestParam(value = "state", required = false, defaultValue = "ACCEPTED") TaskEventFlowState state,
                                                            @RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok(taskService.acceptOrRejectAssignedTask(taskId, state));
    }


    /**
     * cancel task / reassign task api.
     *
     * @param isCancelled
     * @param taskId
     * @param userId
     * @return
     */
    @PostMapping("/cancel-reassign")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> cancelTask(@RequestParam(value = "isCancelled", defaultValue = "true") Boolean isCancelled,
                                                    @RequestParam("taskId") Long taskId,
                                                    @RequestParam(value = "userId", required = false) Long userId) {
        return ResponseEntity.ok(taskService.cancelTask(isCancelled, taskId, userId));
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
     * create new task api.
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
    public ResponseEntity<TasksForProvider> createTask(@RequestBody TaskAssignDto dto) {
        log.info("Call from order-api");
        return ResponseEntity.ok(taskService.fetchProjectedResponseFromPost(dto));
    }
}
