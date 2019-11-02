package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.dao.PaymentDao;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.service.PaymentService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public PaymentServiceImpl(PaymentDao paymentDao,
                              ProjectionFactory projectionFactory) {
        this.paymentDao = paymentDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public Payment create(Task task, TaskAssignDto dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        Boolean isPrepaid = dto.getIsPrepaid();
        payment.setIsPrepaid(isPrepaid);
        payment.setMode(dto.getPaymentMode());
        payment.setState(dto.getPaymentState());
        payment.setTask(task);
        if (BooleanUtils.isTrue(isPrepaid)) payment.setSettleState(SettleState.NOT_APPLICABLE);
        payment.setIsSettled(false);
        return paymentDao.save(payment);
    }

    @Override
    public Payment patch(Task task) {
        Payment payment = task.getPayments().get(0);
        payment.setState(PaymentState.PAID);
        payment.setSettleState(SettleState.PAYMENT_RECEIVED);
        return paymentDao.save(payment);
    }

    @Override
    public List<Payments> fetchSettleRequests(Long userId, SettleState settleState) {
        List<Payment> payments = paymentDao.findBySettleState(settleState);
        List<Payments> paymentResponse = new ArrayList<>();
        Boolean settleFlag = (settleState.equals(SettleState.SETTLED)) ? true : false;
        return settlePayments(userId, payments, paymentResponse, settleFlag);
    }

    @Override
    public List<Payments> settlePayments(Long userId, List<Payment> payments, List<Payments> paymentResponse, Boolean settleFlag) {
        payments.stream().forEach(a -> {
            a.setIsSettled(settleFlag);
            a.setSettleState(SettleState.SETTLED);
            a.setSettledBy(userId);
            paymentResponse.add(projectionFactory.createProjection(Payments.class, paymentDao.save(a)));
        });
        return paymentResponse;
    }


    @Override
    public Payment updateAmount(Task task, Double amount) {
        List<Payment> payments = task.getPayments();
        if (!CollectionUtils.isEmpty(payments)) {
            Payment payment = payments.get(0);
            payment.setAmount(amount);
            return paymentDao.save(payment);
        }
        return null;
    }

    @Override
    public List<Payment> fetchPaymentSettlementsForProvider(Long userId) {
        return paymentDao.findBySettleStateAndPaidBy(SettleState.REQUESTED, userId);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentDao.save(payment);
    }

}
