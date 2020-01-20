package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.PaymentStateFlow;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;

/**
 * PaymentStateFlow response body.
 */
@Projection(types = {PaymentStateFlow.class})
public interface PaymentStateFlows {

    Long getId();

    Timestamp getCreatedAt();

    Double getAmountAdded();


}
