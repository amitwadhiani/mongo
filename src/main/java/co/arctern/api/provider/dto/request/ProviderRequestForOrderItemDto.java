package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto for provider and order-item mapping.
 */
@Data
@NoArgsConstructor
public class ProviderRequestForOrderItemDto {

    private Long orderItemId;
    private Long taskId;
    private String providerName;
    private String providerPhone;
}
