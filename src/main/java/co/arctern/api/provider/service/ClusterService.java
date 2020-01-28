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

    /**
     * fetch cluster by id.
     *
     * @param id
     * @return
     */
    public Cluster fetchById(Long id);

    /**
     * fetch all clusters.
     *
     * @param pageable
     * @return
     */
    public PaginatedResponse fetchAll(Pageable pageable);

    /**
     * fetch cluster.
     *
     * @param id
     * @return
     */
    public Clusters fetch(Long id);

    /**
     * fetch all clusters mapped to a provider.
     *
     * @param id
     * @return
     */
    public List<ClustersWoArea> fetchClustersForProvider(Long id);

    /**
     * edit clusters mapped to a provider.
     *
     * @param userId
     * @param ids
     * @return
     */
    public StringBuilder editClustersForProvider(Long userId, List<Long> ids);

    /**
     * create new clusters.
     *
     * @param dtos
     * @return
     */
    public StringBuilder createClusters(List<ClusterRequestDto> dtos);

    /**
     * create new areas via pinCodes.
     *
     * @param dtos
     * @return
     */
    public StringBuilder createAreas(List<AreaRequestDto> dtos);

    /**
     * fetch areas in a cluster from clusterId.
     *
     * @param clusterId
     * @return
     */
    public List<Areas> fetchAreas(Long clusterId);

}
