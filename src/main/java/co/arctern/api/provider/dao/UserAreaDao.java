package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserArea;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * UserArea entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface UserAreaDao extends PagingAndSortingRepository<UserArea, Long> {
}
