package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.PaymentStateFlow;
import org.springframework.data.rest.core.config.Projection;

/**
 * PaymentStateFlow response body.
 */
@Projection(types = {PaymentStateFlow.class})
public interface PaymentStateFlows {
}
