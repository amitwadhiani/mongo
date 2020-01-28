package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserCluster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserArea entity repository layer
 */
@Repository
public interface UserClusterDao extends PagingAndSortingRepository<UserCluster, Long> {

    /**
     * fetch users active in given areas.
     * @param clusterIds
     * @param pageable
     * @return
     */
    Page<UserCluster> findByClusterIdInAndIsActiveTrue(List<Long> clusterIds, Pageable pageable);

    /**
     *  fetch active userClusters.
     * @param pageable
     * @return
     */
    Page<UserCluster> findByIsActiveTrue(Pageable pageable);
}
