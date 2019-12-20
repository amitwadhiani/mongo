package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.dao.PaymentStateFlowRepository;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.PaymentStateFlow;
import co.arctern.api.provider.service.PaymentStateFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PaymentStateFlowServiceImpl implements PaymentStateFlowService {

    private final PaymentStateFlowRepository paymentStateFlowRepository;

    @Autowired
    public PaymentStateFlowServiceImpl(PaymentStateFlowRepository paymentStateFlowRepository) {
        this.paymentStateFlowRepository = paymentStateFlowRepository;
    }

    @Transactional
    @Override
    public PaymentStateFlow create(Payment payment, PaymentState state, Double amount) {
        PaymentStateFlow paymentStateFlow = new PaymentStateFlow();
        paymentStateFlow.setPayment(payment);
        paymentStateFlow.setState(state);
        if (amount != null) paymentStateFlow.setAmountAdded(amount);
        return paymentStateFlowRepository.save(paymentStateFlow);
    }
}
