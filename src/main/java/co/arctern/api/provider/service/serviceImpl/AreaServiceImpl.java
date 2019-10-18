package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AreaDao;
import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserArea;
import co.arctern.api.provider.dto.request.AreaRequestDto;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.service.AreaService;
import co.arctern.api.provider.service.ClusterService;
import co.arctern.api.provider.service.RoleService;
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
public class AreaServiceImpl implements AreaService {

    private final AreaDao areaDao;
    private final UserAreaDao userAreaDao;
    private final RoleService roleService;
    private final ClusterService clusterService;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public AreaServiceImpl(AreaDao areaDao,
                           UserAreaDao userAreaDao,
                           ProjectionFactory projectionFactory,
                           RoleService roleService,
                           ClusterService clusterService) {
        this.areaDao = areaDao;
        this.userAreaDao = userAreaDao;
        this.projectionFactory = projectionFactory;
        this.roleService = roleService;
        this.clusterService = clusterService;
    }

    @Override
    public void setAreasToUser(User user, List<Long> areaIds, List<Role> roles, Long clusterId) {
        List<Area> areas = new ArrayList<>();
        if (!roles.contains("ROLE_ADMIN")) {
            if (roles.contains("ROLE_CLUSTER_MANAGER")) {
                areas = areaDao.fetchActiveAreasByCluster(clusterId);
            } else {
                areas = areaDao.findByIdIn(areaIds);
            }
        } else {
            areas = areaDao.fetchActiveAreas();
        }
        List<UserArea> userAreas = new ArrayList<>();
        List<UserArea> existingUserAreas = user.getUserAreas();
        if (!CollectionUtils.isEmpty(existingUserAreas)) {
            userAreaDao.deleteAll(existingUserAreas);
        }
        areas.forEach(a -> {
            UserArea userArea = new UserArea();
            userArea.setArea(a);
            userArea.setIsActive(true);
            userArea.setUser(user);
            userAreas.add(userArea);
        });
        userAreaDao.saveAll(userAreas);
    }

    @Override
    @Transactional
    public StringBuilder createAreas(List<AreaRequestDto> dtos) {
        List<Area> areas = new ArrayList<>();
        dtos.stream().forEach(dto ->
        {
            Area area = new Area();
            area.setCluster(clusterService.fetchById(dto.getClusterId()));
            area.setIsActive(true);
            area.setName(dto.getName());
            area.setLatitude(dto.getLatitude());
            area.setLongitude(dto.getLongitude());
            area.setPinCode(dto.getPinCode());
            areas.add(area);
        });
        areaDao.saveAll(areas);
        return SUCCESS_MESSAGE;
    }

    @Override
    public Page<Areas> fetchAreas(Pageable pageable) {
        return areaDao.findByIsActiveTrue(pageable).map(area -> projectionFactory.createProjection(Areas.class, area));
    }

    @Override
    public Area fetchById(Long areaId) {
        return areaDao.findById(areaId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_AREA_ID_MESSAGE.toString());
        });
    }

    @Override
    public List<Area> fetchAreas(List<Long> areaIds) {
        return areaDao.findByIdIn(areaIds);
    }

    @Override
    public void saveAll(List<Area> areas) {
        areaDao.saveAll(areas);
    }

    @Override
    public void save(Area area) {
        areaDao.save(area);
    }

    @Override
    public Page<Areas> search(String pinCode, Pageable pageable) {
        return areaDao.findByPinCodeStartingWith(pinCode, pageable).map(a -> projectionFactory.createProjection(Areas.class, a));
    }


}
