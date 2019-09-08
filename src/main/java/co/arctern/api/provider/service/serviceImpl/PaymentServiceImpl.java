package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.PaymentDao;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentDao paymentDao;

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
}
