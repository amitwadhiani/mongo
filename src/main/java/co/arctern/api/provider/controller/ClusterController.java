package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.ClusterRequestDto;
import co.arctern.api.provider.dto.response.projection.Clusters;
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
@RequestMapping("/cluster")
public class ClusterController {

    private final ClusterService clusterService;

    @Autowired
    public ClusterController(ClusterService clusterService) {
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
    public ResponseEntity<StringBuilder> createClusters(@RequestBody List<ClusterRequestDto> dtos) {
        return ResponseEntity.ok(clusterService.createClusters(dtos));
    }

    /**
     * fetch all clusters.
     *
     * @param pageable
     * @return
     */
    @GetMapping("/fetch-all")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<Clusters>> fetchClusters(Pageable pageable) {
        return ResponseEntity.ok(clusterService.fetchAll(pageable));
    }

    /**
     * fetch cluster.
     *
     * @return
     */
    @GetMapping("/fetch/{id}")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Clusters> fetchClusters(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clusterService.fetch(id));
    }

}
