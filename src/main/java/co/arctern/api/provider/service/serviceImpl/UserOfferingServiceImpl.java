package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserOfferingDao;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.dto.response.projection.Offerings;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.service.UserOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserOfferingServiceImpl implements UserOfferingService {

    @Autowired
    UserOfferingDao userOfferingDao;

    @Autowired
    ProjectionFactory projectionFactory;

    @Override
    public Map<Object, List<Users>> fetchUsersByOffering(Pageable pageable) {
        return userOfferingDao.findByIsActiveTrue(pageable).stream()
                .collect(Collectors.groupingBy(a -> projectionFactory.createProjection(Offerings.class, a.getOffering()), Collectors.mapping(a -> projectionFactory.createProjection(Users.class, a.getUser()), Collectors.toList())));
    }
}
