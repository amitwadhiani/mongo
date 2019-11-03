package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.PaymentStateFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface PaymentStateFlowService extends MessageUtil {

    PaymentStateFlow create(Payment payment, PaymentState state, Double amount);
}
