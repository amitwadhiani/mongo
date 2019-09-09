package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.constant.OfferingType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OfferingRequestDto {

    private Long id;
    private String description;
    private OfferingType type;
}
