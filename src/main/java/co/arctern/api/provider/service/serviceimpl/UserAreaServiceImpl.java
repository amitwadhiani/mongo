package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.dao.UserClusterDao;
import co.arctern.api.provider.domain.UserCluster;
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

    private final UserClusterDao userClusterDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public UserAreaServiceImpl(UserClusterDao userClusterDao,
                               ProjectionFactory projectionFactory) {
        this.userClusterDao = userClusterDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public PaginatedResponse fetchUsersByArea(List<Long> areaIds, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userClusterDao.findByClusterIdInAndIsActiveTrue(areaIds, pageable).map(
                UserCluster::getUser), pageable);
    }

    @Override
    public List<ClustersWoArea> fetchAreasForUser(List<UserCluster> userClusters) {
        if (CollectionUtils.isEmpty(userClusters)) return null;
        return (userClusters.parallelStream()
                .filter(a -> a.getIsActive() && a.getCluster() != null)
                .map(userCluster -> projectionFactory.createProjection(ClustersWoArea.class, userCluster.getCluster()))
                .filter(PaginationUtil.distinctByKey(ClustersWoArea::getId))
                .collect(Collectors.toList()));
    }

}
