package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Cluster;
import co.arctern.api.provider.dto.request.AreaRequestDto;
import co.arctern.api.provider.dto.request.ClusterRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.dto.response.projection.Clusters;
import co.arctern.api.provider.dto.response.projection.ClustersWoArea;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClusterService extends MessageUtil {

    public Cluster fetchById(Long id);

    public PaginatedResponse fetchAll(Pageable pageable);

    public Clusters fetch(Long id);

    public List<ClustersWoArea> fetchClustersForProvider(Long id);

    public StringBuilder editClustersForProvider(Long userId, List<Long> ids);

    public StringBuilder createClusters(List<ClusterRequestDto> dtos);

    public StringBuilder createAreas(List<AreaRequestDto> dtos);

    public List<Areas> fetchAreas(Long clusterId);

}
