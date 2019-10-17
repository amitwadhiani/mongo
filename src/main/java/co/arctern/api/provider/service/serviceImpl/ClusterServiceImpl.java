package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.ClusterDao;
import co.arctern.api.provider.domain.Cluster;
import co.arctern.api.provider.dto.request.ClusterRequestDto;
import co.arctern.api.provider.dto.response.projection.Clusters;
import co.arctern.api.provider.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClusterServiceImpl implements ClusterService {

    private final ClusterDao clusterDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public ClusterServiceImpl(ClusterDao clusterDao,
                              ProjectionFactory projectionFactory) {
        this.clusterDao = clusterDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public Cluster fetchById(Long id) {
        return clusterDao.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_OFFERING_ID_MESSAGE.toString());
        });
    }

    @Override
    public Page<Clusters> fetchAll(Pageable pageable) {
        return clusterDao.findByIsActiveTrue(pageable).map(a -> projectionFactory.createProjection(Clusters.class, a));
    }

    @Override
    public StringBuilder createClusters(List<ClusterRequestDto> dtos) {
        List<Cluster> clusters = new ArrayList<>();
        dtos.stream().forEach(dto -> {
            Cluster cluster = new Cluster();
            cluster.setIsActive(true);
            cluster.setName(dto.getClusterName());
            if (!CollectionUtils.isEmpty(dto.getAreas())) {
                // TODO :: Area creation / attaching areas to cluster
            }
            clusters.add(cluster);
        });
        clusterDao.saveAll(clusters);
        return SUCCESS_MESSAGE;
    }


}
