package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.dao.ClusterDao;
import co.arctern.api.provider.dao.UserClusterDao;
import co.arctern.api.provider.domain.UserCluster;
import co.arctern.api.provider.service.UserClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserClusterServiceImpl implements UserClusterService {


    private final ClusterDao clusterDao;
    private final ProjectionFactory projectionFactory;
    private final UserClusterDao userClusterDao;

    @Autowired
    public UserClusterServiceImpl(ClusterDao clusterDao,
                                  ProjectionFactory projectionFactory,
                                  UserClusterDao userClusterDao) {
        this.clusterDao = clusterDao;
        this.projectionFactory = projectionFactory;
        this.userClusterDao = userClusterDao;
    }

    @Override
    public void deleteAll(List<UserCluster> userClusters) {
        userClusterDao.deleteAll(userClusters);
    }

    @Override
    public void saveAll(List<UserCluster> userClusters) {
        userClusterDao.saveAll(userClusters);
    }

}
