package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.SettleStateFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface SettleStateFlowService extends MessageUtil {

    /**
     * create SettleStateFlow.
     *
     * @param payment
     * @param state
     * @return
     */
    public SettleStateFlow create(Payment payment, SettleState state);
}
