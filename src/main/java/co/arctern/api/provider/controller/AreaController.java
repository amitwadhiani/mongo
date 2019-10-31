package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.AreaRequestDto;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.service.AreaService;
import co.arctern.api.provider.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@BasePathAwareController
@RequestMapping("/area")
public class AreaController {

    private final AreaService areaService;
    private final ClusterService clusterService;

    @Autowired
    public AreaController(AreaService areaService,
                          ClusterService clusterService) {
        this.areaService = areaService;
        this.clusterService = clusterService;
    }

    /**
     * create new areas api.
     *
     * @param dtos
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> createAreas(@RequestBody List<AreaRequestDto> dtos) {
        return ResponseEntity.ok(clusterService.createAreas(dtos));
    }

    /**
     * fetch all areas.
     *
     * @param pageable
     * @return
     */
    @GetMapping("/fetch-all")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<Areas>> fetchAreas(Pageable pageable) {
        return ResponseEntity.ok(areaService.fetchAreas(pageable));
    }

    @GetMapping("/search")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<String>> search(@RequestParam("value") String value) {
        return ResponseEntity.ok(areaService.search(value));
    }

    @GetMapping("/exists")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> pincodeExists(@RequestParam("value") String value) {
        return ResponseEntity.ok(areaService.pincodeExists(value));
    }

    @GetMapping("/fetch/by-pincode")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Areas> fetchAreaByPinCode(@RequestParam("pinCode") String pinCode) {
        return ResponseEntity.ok(areaService.fetchArea(pinCode));
    }

}
