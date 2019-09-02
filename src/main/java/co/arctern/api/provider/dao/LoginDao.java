package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.domain.Login;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Login entity repository layer
 */
@RepositoryRestResource(exported = false)
@PreAuthorize("isAuthenticated()")
public interface LoginDao extends PagingAndSortingRepository<Login, Long> {

    @PreAuthorize("permitAll()")
    Login findByGeneratedOTPAndStatusAndContact(String generatedOTP, OTPState status, String contact);

    Optional<Login> findByUserIdAndStatusAndContact(Long userId, OTPState status, String contact);

    Login findByGeneratedOTPAndStatusAndContactAndCreatedAtGreaterThanEqual(String generatedOTP, OTPState status, String contact,
                                                                            Timestamp createdAt);

    @Override
    @PreAuthorize("permitAll()")
    Login save(Login login);


}
