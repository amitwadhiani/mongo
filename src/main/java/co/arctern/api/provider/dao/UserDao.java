package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * User entity repository layer
 */
@RepositoryRestResource(exported = false)
@PreAuthorize("isAuthenticated()")
public interface UserDao extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    Boolean existsByUsername(String userName);

    Optional<User> findByPhone(String phone);

    void deleteByUsername(String username);

    @Override
    @PreAuthorize("permitAll()")
    Optional<User> findById(Long id);
}
