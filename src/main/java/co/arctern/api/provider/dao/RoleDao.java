package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Role entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface RoleDao extends PagingAndSortingRepository<Role, Long> {
}
