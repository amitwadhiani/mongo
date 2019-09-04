package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserRole;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * UserRole entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface UserRoleDao extends PagingAndSortingRepository<UserRole, Long> {
}
