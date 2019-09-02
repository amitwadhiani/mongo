package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AreaDao;
import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserArea;
import co.arctern.api.provider.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaDao areaDao;

    @Autowired
    UserAreaDao userAreaDao;

    public void setAreasToUser(User user, List<Long> areaIds) {
        List<UserArea> userAreas = new ArrayList<>();
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
}
