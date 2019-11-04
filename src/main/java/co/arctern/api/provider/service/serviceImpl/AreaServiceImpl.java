package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AreaDao;
import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.domain.*;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.dto.response.projection.ClustersWoArea;
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
import java.util.stream.Collectors;

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
        if (!roles.stream().map(a -> a.getRole()).collect(Collectors.toList()).contains("ROLE_ADMIN")) {
            if (clusterId != null) areas = areaDao.fetchActiveAreasByCluster(clusterId);
            if (!CollectionUtils.isEmpty(areaIds)) areas = areaDao.findByIdIn(areaIds);
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
    public List<Area> fetchAreas(List<String> pinCodes) {
        return areaDao.findDistinctByPinCodeIn(pinCodes);
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
    public List<String> search(String value) {
        List<Area> areas = ((value.matches(PIN_CODE_REGEXP)) ?
                areaDao.findByPinCodeStartingWithAndDeliveryStateTrue(value) :
                areaDao.findByNameContainingAndDeliveryStateTrue(value));
        return areas.stream().collect(Collectors
                .groupingBy(a -> a.getPinCode()))
                .keySet()
                .stream()
                .map(a -> a)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean pincodeExists(String value) {
        return areaDao.existsByPinCode(value);
    }

    @Override
    public Areas fetchArea(String pinCode) {
        return projectionFactory.createProjection(Areas.class, areaDao.findByPinCode(pinCode).stream()
                .findFirst()
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_PIN_CODE_MESSAGE.toString());
                }));
    }

    @Override
    public ClustersWoArea getCluster(Address address) {
        if (address != null && address.getArea() != null && address.getArea().getCluster() != null) {
            Cluster cluster = address.getArea().getCluster();
            if (cluster != null) {
                return projectionFactory.createProjection(ClustersWoArea.class, cluster);
            }
            return null;
        }
        return null;
    }
}
