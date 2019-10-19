package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.ClusterDao;
import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.Cluster;
import co.arctern.api.provider.dto.request.AreaRequestDto;
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

import javax.transaction.Transactional;
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
        return clusterDao.findAll(pageable).map(a -> projectionFactory.createProjection(Clusters.class, a));
    }

    @Override
    @Transactional
    public StringBuilder createClusters(List<ClusterRequestDto> dtos) {
        List<Area> areasToSave = new ArrayList<>();
        dtos.stream().forEach(dto -> {
            Cluster cluster = (dto.getClusterId() == null) ? new Cluster() : clusterDao.findById(dto.getClusterId()).get();
            if (dto.getIsActive() != null) cluster.setIsActive(dto.getIsActive());
            if (dto.getClusterName() != null) cluster.setName(dto.getClusterName());
            List<Long> areaIds = dto.getAreaIds();
            cluster = clusterDao.save(cluster);
            /**
             * replace existing areas ( if there ) with new areas.
             */
            if (!CollectionUtils.isEmpty(areaIds)) {
                cluster.getAreas().stream().forEach(a -> {
                    a.setCluster(null);
                    areaService.save(a);
                });
                List<Area> areas = areaService.fetchAreas(areaIds);
                for (Area area : areas) {
                    if (area.getCluster() != null)
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AREA_ALREADY_ASSIGNED_TO_CLUSTER.toString());
                    area.setCluster(cluster);
                    areasToSave.add(area);
                }
            }
        });
        if (!CollectionUtils.isEmpty(areasToSave)) areaService.saveAll(areasToSave);
        return SUCCESS_MESSAGE;
    }

    @Override
    @Transactional
    public StringBuilder createAreas(List<AreaRequestDto> dtos) {
        List<Area> areas = new ArrayList<>();
        dtos.stream().forEach(dto ->
        {
            Area area = new Area();
            area.setCluster(fetchById(dto.getClusterId()));
            area.setIsActive(true);
            area.setName(dto.getName());
            area.setLatitude(dto.getLatitude());
            area.setCircle(dto.getCircle());
            area.setDistrict(dto.getDistrict());
            area.setDeliveryState(dto.getDeliveryState());
            area.setOfficeType(dto.getOfficeType());
            area.setHeadOffice(dto.getHeadOffice());
            area.setSubOffice(dto.getSubOffice());
            area.setState(dto.getState());
            area.setDivision(dto.getDivision());
            area.setPhone(dto.getPhone());
            area.setRegion(dto.getRegion());
            area.setLongitude(dto.getLongitude());
            area.setPinCode(dto.getPincode());
            areas.add(area);
        });
        areaService.saveAll(areas);
        return SUCCESS_MESSAGE;
    }


}
