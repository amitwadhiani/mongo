package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AreaDao;
import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserArea;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.service.AreaService;
import co.arctern.api.provider.service.RoleService;
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
public class AreaServiceImpl implements AreaService {

    private final AreaDao areaDao;
    private final UserAreaDao userAreaDao;
    private final RoleService roleService;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public AreaServiceImpl(AreaDao areaDao,
                           UserAreaDao userAreaDao,
                           ProjectionFactory projectionFactory,
                           RoleService roleService
    ) {
        this.areaDao = areaDao;
        this.userAreaDao = userAreaDao;
        this.projectionFactory = projectionFactory;
        this.roleService = roleService;
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
    public Page<Areas> search(String value, Pageable pageable) {
        if (value.matches("^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$")) {
            return areaDao.findByPinCode(value, pageable).map(a -> projectionFactory.createProjection(Areas.class, a));
        }
        return areaDao.findByNameContaining(value, pageable).map(a -> projectionFactory.createProjection(Areas.class, a));
    }

    @Override
    public Boolean pincodeExists(String value) {
        return areaDao.existsByPinCode(value);
    }

}
