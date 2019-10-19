package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.constant.OfficeType;
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
    private String pincode;
    private String name;
    private Boolean deliveryState;
    private String state;
    private String region;
    private String division;
    private String circle;
    private String phone;
    private String district;
    private String subOffice;
    private String headOffice;
    private OfficeType officeType;
    private Long clusterId;
    private Boolean isActive;
}
