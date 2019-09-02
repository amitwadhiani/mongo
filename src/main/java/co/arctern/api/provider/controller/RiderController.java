package co.arctern.api.provider.controller;

import co.arctern.api.provider.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@BasePathAwareController
@RequestMapping("/rider")
public class RiderController {

    @Autowired
    private RiderService riderService;

    @CrossOrigin
    @GetMapping("/task/all")
    public ResponseEntity<?> fetchTasksForRider(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(riderService.fetchTasksForRider(userId));
    }
}
