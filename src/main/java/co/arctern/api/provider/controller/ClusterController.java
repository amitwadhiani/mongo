package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.ClusterRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Clusters;
import co.arctern.api.provider.dto.response.projection.ClustersWoArea;
import co.arctern.api.provider.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * create new clusters api.
     *
     * @param dtos
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> createClusters(@RequestBody List<ClusterRequestDto> dtos) {
        return ResponseEntity.ok(clusterService.createClusters(dtos));
    }

    /**
     * fetch all clusters.
     *
     * @return
     */
    @GetMapping("/fetch-all")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<PaginatedResponse> fetchClusters(Pageable pageable) {
        return ResponseEntity.ok(clusterService.fetchAll(pageable));
    }

    /**
     * fetch cluster.
     *
     * @return
     */
    @GetMapping("/fetch/{id}")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<Clusters> fetchClusters(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clusterService.fetch(id));
    }

    /**
     * fetch clusters for provider.
     *
     * @return
     */
    @GetMapping("/fetch/by-provider/{id}")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<List<ClustersWoArea>> fetchClustersForProvider(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clusterService.fetchClustersForProvider(id));
    }

    /**
     * fetch clusters for provider.
     *
     * @return
     */
    @PostMapping("/edit/by-provider")
    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<StringBuilder> editClustersForProvider(@RequestBody ClusterRequestDto dto) {
        return ResponseEntity.ok(clusterService.editClustersForProvider(dto.getUserId(), dto.getIds()));
    }

}
