package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Area;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Area entity repository layer
 */
@Repository
@PreAuthorize("isAuthenticated()")
public interface AreaDao extends PagingAndSortingRepository<Area, Long> {

    List<Area> findByIdIn(List<Long> areaIds);
}
