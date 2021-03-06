package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Role entity repository layer
 */
@Repository
public interface RoleDao extends PagingAndSortingRepository<Role, Long> {

    /**
     * fetch roles through ids.
     * @param roleIds
     * @return
     */
    List<Role> findByIdIn(List<Long> roleIds);
}
