package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.ClusterDao;
import co.arctern.api.provider.domain.Cluster;
import co.arctern.api.provider.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClusterServiceImpl implements ClusterService {

    private final ClusterDao clusterDao;

    @Autowired
    public ClusterServiceImpl(ClusterDao clusterDao) {
        this.clusterDao = clusterDao;
    }

    @Override
    public Cluster fetchById(Long id) {
        return clusterDao.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_OFFERING_ID_MESSAGE.toString());
        });
    }

}
