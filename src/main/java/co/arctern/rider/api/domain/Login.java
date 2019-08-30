package co.arctern.rider.api.domain;

import co.arctern.rider.api.enums.OTPStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OTPStatus status;
    private String generatedOTP;
    private String contact;

}
