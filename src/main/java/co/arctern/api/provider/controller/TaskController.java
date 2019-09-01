package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@BasePathAwareController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/assign")
    @CrossOrigin
    public ResponseEntity<StringBuilder> assignTaskToUser(@RequestBody TaskAssignDto dto) {
        return ResponseEntity.ok(taskService.createTaskAndAssignUser(dto));
    }


    @PostMapping("/reassign")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> reassignTaskToUser(@RequestParam("taskId") Long taskId,
                                                            @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(taskService.reassignTask(taskId, userId));
    }

    @PostMapping("/cancel-reassign")
    @CrossOrigin
    public ResponseEntity<StringBuilder> cancelTask(@RequestParam(value = "isCancelled", defaultValue = "true") Boolean isCancelled,
                                                    @RequestParam("taskId") Long taskId,
                                                    @RequestParam(value = "userId", required = false) Long userId) {
        return ResponseEntity.ok(taskService.cancelTask(isCancelled, taskId, userId));
    }

    @PostMapping("/cancel-request")
    @CrossOrigin
    public ResponseEntity<StringBuilder> requestToCancelTask(@RequestParam(value = "cancelRequest", defaultValue = "true") Boolean cancelRequest,
                                                             @RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok(taskService.requestCancellation(cancelRequest, taskId));
    }

}
