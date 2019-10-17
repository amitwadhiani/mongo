package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * area creation request body.
 */
@Data
@NoArgsConstructor
public class AreaRequestDto {

    private Double latitude;
    private Double longitude;
    private String pinCode;
    private String name;
    private Long clusterId;
    private Boolean isActive;
}
