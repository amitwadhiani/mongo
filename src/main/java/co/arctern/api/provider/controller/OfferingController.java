package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.OfferingRequestDto;
import co.arctern.api.provider.dto.response.projection.Offerings;
import co.arctern.api.provider.service.OfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@BasePathAwareController
@RequestMapping("/offering")
public class OfferingController {

    @Autowired
    OfferingService offeringService;

    @CrossOrigin
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> postOfferings(@RequestBody List<OfferingRequestDto> dtos) {
        return ResponseEntity.ok(offeringService.create(dtos));
    }

    @CrossOrigin
    @GetMapping("/fetch/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Offerings>> fetchAllOfferings() {
        return ResponseEntity.ok(offeringService.fetchAll());
    }

    @CrossOrigin
    @GetMapping("/fetch/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Offerings> fetchOfferingById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(offeringService.fetchById(id));
    }

}
