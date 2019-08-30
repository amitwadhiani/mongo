package co.arctern.rider.api.dao;

import co.arctern.rider.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    Boolean existsByUsername(String userName);

    Optional<User> findByPhone(String phone);

    void deleteByUsername(String username);


}
