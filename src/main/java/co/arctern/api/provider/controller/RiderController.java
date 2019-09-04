package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * apis for rider's tasks.
 */
@BasePathAwareController
@RequestMapping("/rider")
public class RiderController {

    @Autowired
    private RiderService riderService;

    /**
     * fetch all tasks for a rider.
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/all")
    public ResponseEntity<TasksForRiderResponse> fetchTasksForRider(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(riderService.fetchTasksForRider(userId));
    }

    /**
     * fetch assigned tasks for a rider.
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/assigned")
    public ResponseEntity<List<TasksForRider>> fetchAssignedTasksForRider(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(riderService.fetchAssignedTasksForRider(userId));
    }

    /**
     * fetch completed tasks for a rider.
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/task/completed")
    public ResponseEntity<List<TasksForRider>> fetchCompletedTasksForRider(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(riderService.fetchCompletedTasksForRider(userId));
    }


}
