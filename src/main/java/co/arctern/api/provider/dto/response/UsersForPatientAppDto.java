package co.arctern.api.provider.dto.response;


import lombok.Data;

/**
 * user entity custom response for patient App
 */
@Data
public class UsersForPatientAppDto {
    private Long orderItemId;
    private Long taskId;
    private String name;
    private String phone;
}
