package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.domain.UserArea;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.ClustersWoArea;
import co.arctern.api.provider.service.UserAreaService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAreaServiceImpl implements UserAreaService {

    private final UserAreaDao userAreaDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public UserAreaServiceImpl(UserAreaDao userAreaDao,
                               ProjectionFactory projectionFactory) {
        this.userAreaDao = userAreaDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public PaginatedResponse fetchUsersByArea(List<Long> areaIds, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userAreaDao.findByAreaIdInAndIsActiveTrue(areaIds, pageable).map(
                UserArea::getUser), pageable);
    }

    @Override
    public List<ClustersWoArea> fetchAreasForUser(List<UserArea> userAreas) {
        if (CollectionUtils.isEmpty(userAreas)) return null;
        return (userAreas.parallelStream()
                .filter(UserArea::getIsActive)
                .map(userArea -> projectionFactory.createProjection(ClustersWoArea.class, userArea.getArea().getCluster()))
                .filter(PaginationUtil.distinctByKey(ClustersWoArea::getId)).collect(Collectors.toList()));
    }

}
