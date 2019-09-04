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
public interface UserDao extends PagingAndSortingRepository<User, Long> {

    /**
     * fetch by username.
     *
     * @param userName
     * @return
     */
    Optional<User> findByUsername(String userName);

    /**
     * exists by username.
     *
     * @param userName
     * @return
     */
    Boolean existsByUsername(String userName);

    /**
     * fetch user through phone.
     *
     * @param phone
     * @return
     */
    @PreAuthorize("permitAll()")
    Optional<User> findByPhone(String phone);

    /**
     * delete user by username.
     *
     * @param username
     */
    void deleteByUsername(String username);

    /**
     * fetch user by id.
     *
     * @param id
     * @return
     */
    @Override
    @PreAuthorize("permitAll()")
    Optional<User> findById(Long id);
}
