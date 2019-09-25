package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * area creation request body.
 */
@Data
@NoArgsConstructor
public class AreaRequestDto {

    private String cluster;
    private Double latitude;
    private Double longitude;
    private String pinCode;
    private Boolean isActive;
}
