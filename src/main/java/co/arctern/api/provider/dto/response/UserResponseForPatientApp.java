package co.arctern.api.provider.dto.response;

import lombok.Data;

/**
 * user entity custom response for patient app
 */
@Data
public class UserResponseForPatientApp {
    private String userName;
    private String userPhone;
}
