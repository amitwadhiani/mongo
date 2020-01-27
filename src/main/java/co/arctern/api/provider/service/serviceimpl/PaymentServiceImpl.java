package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.dao.PaymentDao;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.service.PaymentService;
import co.arctern.api.provider.service.PaymentStateFlowService;
import co.arctern.api.provider.service.SettleStateFlowService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentDao;
    private final ProjectionFactory projectionFactory;
    private final PaymentStateFlowService paymentStateFlowService;
    private final SettleStateFlowService settleStateFlowService;

    @Autowired
    public PaymentServiceImpl(PaymentDao paymentDao,
                              ProjectionFactory projectionFactory,
                              PaymentStateFlowService paymentStateFlowService,
                              SettleStateFlowService settleStateFlowService) {
        this.paymentDao = paymentDao;
        this.projectionFactory = projectionFactory;
        this.paymentStateFlowService = paymentStateFlowService;
        this.settleStateFlowService = settleStateFlowService;
    }

    @Override
    public Payment create(Task task, TaskAssignDto dto) {
        Boolean isPrepaid = BooleanUtils.isTrue(dto.getIsPrepaid());
        PaymentState paymentState = dto.getPaymentState();
        Payment payment = this.createPayment(task, dto, isPrepaid, paymentState);
        createSettleStateFlow(payment, isPrepaid ? SettleState.NOT_APPLICABLE : SettleState.PENDING);
        createPaymentStateFlow(payment, paymentState, dto.getAmount());
        return payment;
    }

    @Override
    public Payment createPayment(Task task, TaskAssignDto dto, Boolean isPrepaid, PaymentState paymentState) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setIsPrepaid(isPrepaid);
        payment.setMode(dto.getPaymentMode());
        payment.setState(paymentState);
        payment.setTask(task);
        payment.setSettleState(isPrepaid ? SettleState.NOT_APPLICABLE : SettleState.PENDING);
        payment.setIsSettled(false);
        payment = paymentDao.save(payment);
        return payment;
    }

    @Override
    public void createPaymentStateFlow(Payment payment, PaymentState paymentState, Double amount) {
        paymentStateFlowService.create(payment, paymentState, amount);
    }

    @Override
    public void createSettleStateFlow(Payment payment, SettleState notApplicable) {
        settleStateFlowService.create(payment, notApplicable);
    }

    @Override
    @Transactional
    public Payment patch(Task task, Long userId) {
        Payment payment = task.getPayments().get(0);
        if (payment.getState().equals(PaymentState.PAID)) return payment;
        payment.setState(PaymentState.PAID);
        payment.setSettleState(SettleState.PAYMENT_RECEIVED);
        payment.setPaidBy(userId);
        payment = paymentDao.save(payment);
        createSettleStateFlow(payment, SettleState.PAYMENT_RECEIVED);
        createPaymentStateFlow(payment, PaymentState.PAID, null);
        return payment;
    }

    @Override
    public List<Payments> fetchSettleRequests(Long userId, SettleState settleState) {
        List<Payment> payments = paymentDao.findBySettleState(settleState);
        List<Payments> paymentResponse = new ArrayList<>();
        Boolean settleFlag = (settleState.equals(SettleState.SETTLED)) ? true : false;
        return settlePayments(userId, payments, paymentResponse, settleFlag);
    }

    @Override
    @Transactional
    public List<Payments> settlePayments(Long userId, List<Payment> payments, List<Payments> paymentResponse, Boolean settleFlag) {
        payments.stream().forEach(payment -> {
            payment.setIsSettled(settleFlag);
            payment.setSettleState(SettleState.SETTLED);
            payment.setSettledBy(userId);
            payment = paymentDao.save(payment);
            createSettleStateFlow(payment, SettleState.SETTLED);
            paymentResponse.add(projectionFactory.createProjection(Payments.class, payment));
        });
        return paymentResponse;
    }


    @Override
    @Transactional
    public Payment updateAmount(Task task, Double amount) {
        List<Payment> payments = task.getPayments();
        if (CollectionUtils.isEmpty(payments)) return null;

        Payment payment = payments.get(0);
        payment.setAmount(amount);
        payment = paymentDao.save(payment);
        createPaymentStateFlow(payment, payment.getState(), amount - payment.getAmount());
        return payment;
    }

    @Override
    public List<Payment> fetchPaymentSettlementsForProvider(Long userId) {
        /**
         * to be changed to Settle state = REQUESTED later.
         */
        return paymentDao.findBySettleStateAndPaidBy(SettleState.PAYMENT_RECEIVED, userId);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentDao.save(payment);
    }

}
