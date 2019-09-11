package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dto.response.HomePageResponse;
import co.arctern.api.provider.service.HomePageService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Override
    public HomePageResponse fetchHomePage(Long userId, ZonedDateTime start, ZonedDateTime end) {
        HomePageResponse response = new HomePageResponse();
//        response.setAssignedTasks(taskService.);
//        response.setCompletedTasks();
//        response.setUpcomingTasks();
        return response;
    }
}
