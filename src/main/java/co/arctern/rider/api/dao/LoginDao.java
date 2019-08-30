package co.arctern.rider.api.dao;

import co.arctern.rider.api.domain.Login;
import co.arctern.rider.api.enums.OTPState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface LoginDao extends JpaRepository<Login, Long> {

    Login findByGeneratedOTPAndStatusAndContact(String generatedOTP, OTPState status, String contact);


}
