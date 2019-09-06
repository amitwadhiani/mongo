package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserArea;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * UserArea entity repository layer
 */
@Repository
public interface UserAreaDao extends PagingAndSortingRepository<UserArea, Long> {
}
