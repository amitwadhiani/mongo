package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Cluster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Cluster entity repository layer
 */
@Repository
public interface ClusterDao extends PagingAndSortingRepository<Cluster, Long> {

    /**
     * fetch active clusters.
     *
     * @param pageable
     * @return
     */
    Page<Cluster> findByIsActiveTrue(Pageable pageable);

    /**
     * fetch all clusters.
     *
     * @return
     */
    List<Cluster> findAll();

    /**
     * fetch active clusters.
     *
     * @return
     */
    @Query("FROM Cluster cluster WHERE cluster.isActive = 1 ")
    List<Cluster> fetchActiveClusters();

    /**
     * fetch clusters by ids.
     *
     * @param clusterIds
     * @return
     */
    List<Cluster> findByIdIn(List<Long> clusterIds);
}
