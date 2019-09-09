package co.arctern.api.provider.controller;

import co.arctern.api.provider.service.OfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;

@BasePathAwareController
@RequestMapping("/offering")
public class OfferingController {

    @Autowired
    OfferingService offeringService;


}
