package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.service.UserAreaService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserAreaServiceImpl implements UserAreaService {

    @Autowired
    private UserAreaDao userAreaDao;

    @Autowired
    private ProjectionFactory projectionFactory;

    @Override
    public PaginatedResponse fetchUsersByArea(List<Long> areaIds, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userAreaDao.findByAreaIdInAndIsActiveTrue(areaIds, pageable).map(
                a -> a.getUser()), pageable);
    }

}
