package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.ReasonEditBody;
import co.arctern.api.provider.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@BasePathAwareController
@RequestMapping("/reason")
public class ReasonController {

    private final ReasonService reasonService;

    @Autowired
    public ReasonController(ReasonService reasonService) {
        this.reasonService = reasonService;
    }

    /**
     * create new reasons api.
     *
     * @param reasons
     * @return
     */
    @CrossOrigin
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> createReasons(@RequestBody List<String> reasons) {
        return ResponseEntity.ok(reasonService.create(reasons));
    }

    /**
     * edit reason api.
     * @param bodies
     * @return
     */
    @CrossOrigin
    @PatchMapping("/edit")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> editReasons(@RequestBody List<ReasonEditBody> bodies) {
        return ResponseEntity.ok(reasonService.edit(bodies));
    }

    /**
     * fetch all reasons api.
     *
     * @return
     */
    @CrossOrigin
    @GetMapping("/fetch/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<?> fetchAllReasons(@RequestParam(value = "isAdmin", required = false, defaultValue = "true") Boolean isAdmin) {
        return ResponseEntity.ok(reasonService.fetchAll(isAdmin));
    }

}
