package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AreaDao;
import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserArea;
import co.arctern.api.provider.dto.request.AreaRequestDto;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.service.AreaService;
import com.amazonaws.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaDao areaDao;

    @Autowired
    UserAreaDao userAreaDao;

    @Autowired
    ProjectionFactory projectionFactory;

    @Override
    public void setAreasToUser(User user, List<Long> areaIds) {
        List<UserArea> userAreas = new ArrayList<>();
        List<UserArea> existingUserAreas = user.getUserAreas();
        if (!CollectionUtils.isNullOrEmpty(existingUserAreas)) {
            existingUserAreas.stream().forEach(a -> a.setIsActive(false));
            userAreaDao.saveAll(existingUserAreas);
        }
        areaDao.findByIdIn(areaIds)
                .forEach(a -> {
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
            area.setCluster(dto.getCluster());
            area.setIsActive(true);
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
        return areaDao.findByIsActiveTrue(pageable).map(area -> projectionFactory.createProjection(Areas.class,area));
    }

}
