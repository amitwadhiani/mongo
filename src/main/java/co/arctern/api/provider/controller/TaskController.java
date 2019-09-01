package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<StringBuilder> reassignTaskToUser(@RequestParam("taskId") Long taskId,
                                                            @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(taskService.reassignTask(taskId,userId));
    }
}
