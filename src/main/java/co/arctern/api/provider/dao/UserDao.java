package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * User entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface UserDao extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    Boolean existsByUsername(String userName);

    Optional<User> findByPhone(String phone);

    void deleteByUsername(String username);


}
