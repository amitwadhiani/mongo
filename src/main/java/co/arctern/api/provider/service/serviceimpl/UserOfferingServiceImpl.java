package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.dao.UserOfferingDao;
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

    private final UserOfferingDao userOfferingDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public UserOfferingServiceImpl(UserOfferingDao userOfferingDao,
                                   ProjectionFactory projectionFactory) {
        this.userOfferingDao = userOfferingDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public Map<Object, List<Users>> fetchUsersByOffering(Pageable pageable) {
        return userOfferingDao.findByIsActiveTrue(pageable).stream()
                .collect(Collectors.groupingBy(userOffering -> projectionFactory.createProjection(Offerings.class, userOffering.getOffering()), Collectors.mapping(userOffering -> projectionFactory.createProjection(Users.class, userOffering.getUser()), Collectors.toList())));
    }
}
