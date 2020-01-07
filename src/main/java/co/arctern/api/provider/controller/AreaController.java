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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<Page<Areas>> fetchAreas(Pageable pageable) {
        return ResponseEntity.ok(areaService.fetchAreas(pageable));
    }

    /**
     * search areas by pinCode api.
     *
     * @param value
     * @return
     */
    @GetMapping("/search")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<List<String>> search(@RequestParam("value") String value) {
        return ResponseEntity.ok(areaService.search(value));
    }

    /**
     * exists by pinCode api.
     * @param value
     * @return
     */
    @GetMapping("/exists")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> pincodeExists(@RequestParam("value") String value) {
        return ResponseEntity.ok(areaService.pincodeExists(value));
    }

    /**
     * fetch area by pinCode api.
     * @param pinCode
     * @return
     */
    @GetMapping("/fetch/by-pincode")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Areas> fetchAreaByPinCode(@RequestParam("pinCode") String pinCode) {
        return ResponseEntity.ok(areaService.fetchArea(pinCode));
    }


}
