package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.constant.OfferingType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * offering creation request body.
 */
@Data
@NoArgsConstructor
public class OfferingRequestDto {

    private Long id;
    private String description;
    private OfferingType type;
}
