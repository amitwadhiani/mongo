package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.dao.SettleStateFlowRepository;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.SettleStateFlow;
import co.arctern.api.provider.service.SettleStateFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SettleStateFlowServiceImpl implements SettleStateFlowService {

    private final SettleStateFlowRepository settleStateFlowRepository;

    @Autowired
    public SettleStateFlowServiceImpl(SettleStateFlowRepository settleStateFlowRepository) {
        this.settleStateFlowRepository = settleStateFlowRepository;
    }

    @Transactional
    @Override
    public SettleStateFlow create(Payment payment, SettleState state) {
        SettleStateFlow settleStateFlow = new SettleStateFlow();
        settleStateFlow.setPayment(payment);
        settleStateFlow.setState(state);
        return settleStateFlowRepository.save(settleStateFlow);
    }
}
