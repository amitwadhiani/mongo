package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.dao.PaymentDao;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    final
    PaymentDao paymentDao;

    @Autowired
    public PaymentServiceImpl(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    @Override
    public Payment create(Task task, TaskAssignDto dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setIsPrepaid(dto.getIsPrepaid());
        payment.setMode(dto.getPaymentMode());
        payment.setState(dto.getPaymentState());
        payment.setTask(task);
        return paymentDao.save(payment);
    }

    @Override
    public Payment patch(Task task) {
        Payment payment = task.getPayments().get(0);
        payment.setState(PaymentState.PAID);
        return paymentDao.save(payment);
    }
}
