package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;

@BasePathAwareController
@RequestMapping("/home")
public class HomePageController {

    @Autowired
    HomePageService homePageService;

    @CrossOrigin
    @GetMapping("/tasks")
    public ResponseEntity<HomePageResponse> fetchHomePage(@RequestParam("userId") Long userId,
                                                          @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
                                                          @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end) {
        if (start == null) start = ZonedDateTime.now();
        if (end == null) end = start.plusDays(2);
        return ResponseEntity.ok(homePageService.fetchHomePage(userId, DateUtil.zonedDateTimeToTimestampConversion(start), DateUtil.zonedDateTimeToTimestampConversion(end)));
    }
}
