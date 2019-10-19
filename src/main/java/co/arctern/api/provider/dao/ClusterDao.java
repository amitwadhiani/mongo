package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Cluster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Cluster entity repository layer
 */
@Repository
public interface ClusterDao extends PagingAndSortingRepository<Cluster, Long> {

    Page<Cluster> findByIsActiveTrue(Pageable pageable);
}
