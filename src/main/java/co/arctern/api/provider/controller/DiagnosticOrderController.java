package co.arctern.api.provider.controller;

import co.arctern.api.internal.api.emr.model.ResourcesPatient;
import co.arctern.api.provider.service.DiagnosticOrderService;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@BasePathAwareController
@RequestMapping("/diagnostic")
public class DiagnosticOrderController {

    public DiagnosticOrderService diagnosticOrderService;

    @GetMapping("/search")
    @CrossOrigin
    public ResponseEntity<ResourcesPatient> searchPatient (@RequestParam("phone") String phone,
                                                           @RequestParam("name") String name,
                                                           Pageable pageable) {
        return ResponseEntity.ok(diagnosticOrderService.searchPatient(phone, name, pageable));
    }


}
