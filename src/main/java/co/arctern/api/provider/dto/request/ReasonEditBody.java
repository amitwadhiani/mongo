package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * reason edit body.
 */
@Data
@NoArgsConstructor
public class ReasonEditBody {

    private Long reasonId;
    private Boolean isActive;
}
