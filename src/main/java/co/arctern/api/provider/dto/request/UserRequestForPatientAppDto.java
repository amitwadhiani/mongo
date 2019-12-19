package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * user request for patient App
 */
@Data
@NoArgsConstructor
public class UserRequestForPatientAppDto {
    private Long orderItemId;
    private Long taskId;
}
