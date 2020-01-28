package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.UserCluster;

import java.util.List;

public interface UserClusterService {

    /**
     * delete all clusters mapped to an user.
     *
     * @param userClusters
     */
    public void deleteAll(List<UserCluster> userClusters);

    /**
     * map/save all clusters and an user.
     *
     * @param userClusters
     */
    public void saveAll(List<UserCluster> userClusters);

}
