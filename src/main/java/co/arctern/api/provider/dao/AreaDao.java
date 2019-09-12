package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Area;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Area entity repository layer
 */
@Repository
public interface AreaDao extends PagingAndSortingRepository<Area, Long> {

    /**
     * fetch areas through ids.
     * @param areaIds
     * @return
     */
    List<Area> findByIdIn(List<Long> areaIds);
}
