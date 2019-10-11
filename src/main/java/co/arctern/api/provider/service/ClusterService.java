package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Cluster;
import co.arctern.api.provider.util.MessageUtil;

public interface ClusterService extends MessageUtil {

    public Cluster fetchById(Long id);


}
