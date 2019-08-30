package co.arctern.rider.api.controller;

import co.arctern.rider.api.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;

@BasePathAwareController
public class RatingController {

    @Autowired
    RatingService ratingService;
}
