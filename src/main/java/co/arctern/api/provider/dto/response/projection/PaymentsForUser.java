package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.domain.Payment;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * Payment response body.
 */
@Projection(types = Payment.class)
public interface PaymentsForUser {

    Long getId();

    Double getAmount();

    SettleState getSettleState();

    PaymentState getState();

    Boolean getIsSettled();

    List<PaymentStateFlows> getPaymentStateFlows();

    List<SettleStateFlows> getSettleStateFlows();

}
