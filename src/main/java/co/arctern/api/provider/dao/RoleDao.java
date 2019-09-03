package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Role entity repository layer
 */
@RepositoryRestResource(exported = false)
@PreAuthorize("isAuthenticated()")
public interface RoleDao extends PagingAndSortingRepository<Role, Long> {

    List<Role> findByIdIn(List<Long> roleIds);
}
