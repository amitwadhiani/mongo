package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.domain.Login;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Login entity repository layer
 */
@Repository
public interface LoginDao extends PagingAndSortingRepository<Login, Long> {

    /**
     * fetch active login through phone and otp.
     *
     * @param generatedOTP
     * @param status
     * @param contact
     * @return
     */
    @PreAuthorize("permitAll()")
    Login findByGeneratedOTPAndStatusAndContact(String generatedOTP, OTPState status, String contact);

    /**
     * fetch login based on status and contact.
     *
     * @param userId
     * @param status
     * @param contact
     * @return
     */
    Optional<Login> findByUserIdAndStatusAndContact(Long userId, OTPState status, String contact);

    List<Login> findByUserIdAndStatusAndContactOrderByCreatedAtDesc(Long userId, OTPState status, String contact);

    /**
     * fetch active login through phone and otp within a given time frame.
     *
     * @param generatedOTP
     * @param status
     * @param contact
     * @param createdAt
     * @return
     */
    Login findByGeneratedOTPAndStatusAndContactAndCreatedAtGreaterThanEqual(String generatedOTP, OTPState status, String contact,
                                                                            Timestamp createdAt);

    /**
     * save login for a user( or phone).
     *
     * @param login
     * @return
     */
    @Override
    @PreAuthorize("permitAll()")
    Login save(Login login);


}
