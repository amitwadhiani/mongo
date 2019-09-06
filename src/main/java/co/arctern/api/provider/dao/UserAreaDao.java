package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserArea entity repository layer
 */
@Repository
public interface UserAreaDao extends PagingAndSortingRepository<UserArea, Long> {

    Page<UserArea> findByAreaIdInAndIsActiveTrue(List<Long> areaIds, Pageable pageable);
}
