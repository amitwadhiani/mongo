package co.arctern.api.provider.dto.response;


import lombok.Data;

/**
 * user entity custom response for patient App
 */
@Data
public class UserResponseForPatientAppDto {

    private Long orderItemid;
    private String name;
    private String phone;
}
