package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User entity repository layer
 */
@Repository
public interface UserDao extends PagingAndSortingRepository<User, Long> {

    /**
     * fetch by username.
     *
     * @param userName
     * @return
     */
    Optional<User> findByUsername(String userName);

    /**
     * exists by phone.
     *
     * @param phone
     * @return
     */
    Boolean existsByPhone(String phone);

    /**
     * exists by email.
     *
     * @param email
     * @return
     */
    Boolean existsByEmail(String email);

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
     * fetch active user through phone.
     *
     * @param phone
     * @return
     */
    Optional<User> findByPhoneAndIsActiveTrue(String phone);

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

    /**
     * fetch active users.
     *
     * @return
     */
    @Query("FROM User u WHERE u.isActive = 1 ")
    List<User> fetchActiveUsers();

    /**
     * fetch active users.
     *
     * @param pageable
     * @return
     */
    Page<User> findByIsActiveTrue(Pageable pageable);

    Page<User> findById(Long id, Pageable pageable);

    Page<User> findByNameStartingWith(String name, Pageable pageable);

}
