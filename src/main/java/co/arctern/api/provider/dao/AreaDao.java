package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Area entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface AreaDao extends PagingAndSortingRepository<Area, Long> {
}
