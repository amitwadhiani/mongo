package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Area entity repository layer
 */
@Repository
public interface AreaDao extends PagingAndSortingRepository<Area, Long> {

    /**
     * fetch areas through ids.
     *
     * @param areaIds
     * @return
     */
    List<Area> findByIdIn(List<Long> areaIds);

    /**
     * fetch active areas.
     *
     * @param pageable
     * @return
     */
    Page<Area> findByIsActiveTrue(Pageable pageable);

    /**
     * fetch active areas.
     *
     * @return
     */
    @Query("FROM Area a WHERE a.isActive =1 ")
    List<Area> fetchActiveAreas();


    /**
     * fetch active areas.
     *
     * @return
     */
    @Query("FROM Area a WHERE (a.isActive =1 AND a.cluster.id = :clusterId ) ")
    List<Area> fetchActiveAreasByCluster(@Param("clusterId") Long clusterId);
}
