package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface PaymentService extends MessageUtil {

    /**
     * create payment for a task.
     *
     * @param task
     * @param dto
     * @return
     */
    Payment create(Task task, TaskAssignDto dto);

    /**
     * patch payment at task completion.
     *
     * @param task
     * @return
     */
    Payment patch(Task task, Long userId);

    /**
     * fetch payments ( settle requests ).
     *
     * @param userId
     * @param settleState
     * @return
     */
    List<Payments> fetchSettleRequests(Long userId, SettleState settleState);

    /**
     * update amount for user after task completion.
     *
     * @param task
     * @param amount
     * @return
     */
    Payment updateAmount(Task task, Double amount);

    /**
     * fetch list of payments and settle.
     *
     * @param userId
     * @param payments
     * @param paymentResponse
     * @param settleFlag
     * @return
     */
    List<Payments> settlePayments(Long userId, List<Payment> payments, List<Payments> paymentResponse, Boolean settleFlag);

    /**
     * fetch Payment settlements for provider.
     *
     * @param userId
     * @return
     */
    List<Payment> fetchPaymentSettlementsForProvider(Long userId);

    /**
     * save payment.
     *
     * @param payment
     * @return
     */
    Payment save(Payment payment);

    /**
     * create PaymentStateFlow.
     *
     * @param payment
     * @param paymentState
     * @param amount
     */
    public void createPaymentStateFlow(Payment payment, PaymentState paymentState, Double amount);

    /**
     * create SettleStateFlow.
     *
     * @param payment
     * @param notApplicable
     */
    public void createSettleStateFlow(Payment payment, SettleState notApplicable);

    /**
     * create Payment for a task.
     *
     * @param task
     * @param dto
     * @param isPrepaid
     * @param paymentState
     * @return
     */
    public Payment createPayment(Task task, TaskAssignDto dto, Boolean isPrepaid, PaymentState paymentState);

}
