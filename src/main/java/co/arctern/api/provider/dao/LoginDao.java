package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.domain.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Login entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface LoginDao extends PagingAndSortingRepository<Login, Long> {

    Login findByGeneratedOTPAndStatusAndContact(String generatedOTP, OTPState status, String contact);


}
