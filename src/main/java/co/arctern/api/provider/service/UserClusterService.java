package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.UserCluster;

import java.util.List;

public interface UserClusterService {

    public void deleteAll(List<UserCluster> userClusters);

    public void saveAll(List<UserCluster> userClusters);

}
