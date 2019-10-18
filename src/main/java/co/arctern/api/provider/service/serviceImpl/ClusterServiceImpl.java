package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.ClusterDao;
import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.Cluster;
import co.arctern.api.provider.dto.request.ClusterRequestDto;
import co.arctern.api.provider.dto.response.projection.Clusters;
import co.arctern.api.provider.service.AreaService;
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
    private final AreaService areaService;

    @Autowired
    public ClusterServiceImpl(ClusterDao clusterDao,
                              ProjectionFactory projectionFactory,
                              AreaService areaService) {
        this.clusterDao = clusterDao;
        this.areaService = areaService;
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
        List<Area> areasToSave = new ArrayList<>();
        dtos.stream().forEach(dto -> {
            Cluster cluster = new Cluster();
            cluster.setIsActive(true);
            cluster.setName(dto.getClusterName());
            List<Long> areaIds = dto.getAreaIds();
            cluster = clusterDao.save(cluster);
            if (!CollectionUtils.isEmpty(areaIds)) {
                List<Area> areas = areaService.fetchAreas(areaIds);
                for (Area area : areas) {
                    area.setCluster(cluster);
                    areasToSave.add(area);
                }
            }
        });
        areaService.saveAll(areasToSave);
        return SUCCESS_MESSAGE;
    }


}
