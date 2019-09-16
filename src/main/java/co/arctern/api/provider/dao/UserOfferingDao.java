package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserOffering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserOffering entity repository layer
 */
@Repository
public interface UserOfferingDao extends PagingAndSortingRepository<UserOffering, Long> {

    /**
     * fetch users by filtered offerings.
     * @param offeringIds
     * @param pageable
     * @return
     */
    Page<UserOffering> findByOfferingIdInAndIsActiveTrue(List<Long>offeringIds, Pageable pageable);

    Page<UserOffering>  findByIsActiveTrue(Pageable pageable);
}
