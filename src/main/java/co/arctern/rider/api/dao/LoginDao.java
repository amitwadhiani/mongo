package co.arctern.rider.api.dao;

import co.arctern.rider.api.domain.Login;
import co.arctern.rider.api.enums.OTPStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LoginDao extends JpaRepository<Login, Long> {

    Login findByGeneratedOTPAndStatusAndContact(String generatedOTP, OTPStatus status,String contact);


}
