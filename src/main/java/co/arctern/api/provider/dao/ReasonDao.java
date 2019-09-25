package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Reason;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Reason entity repository layer
 */
@Repository
public interface ReasonDao extends PagingAndSortingRepository<Reason, Long> {

    /**
     * fetch reasons through ids.
     * @param reasonIds
     * @return
     */
    List<Reason> findByIdIn(List<Long> reasonIds);
}
