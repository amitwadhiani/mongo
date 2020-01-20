package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * payment entity repository layer.
 */
@Repository
public interface PaymentDao extends PagingAndSortingRepository<Payment, Long> {

    /**
     * fetch payments by settleState
     *
     * @param settleState
     * @return
     */
    List<Payment> findBySettleState(SettleState settleState);

    /**
     * fetch payments by settleState and paidBy (userId).
     *
     * @param settleState
     * @param paidBy
     * @return
     */
    List<Payment> findBySettleStateAndPaidBy(SettleState settleState, Long paidBy);

    @Query("FROM Payment payment " +
            "WHERE payment.task.activeUserId = :userId " +
            "AND payment.task.state = :state " +
            "ORDER BY payment.createdAt DESC ")
    public Page<Payment> fetchPaymentsForUser(Long userId, TaskState state, Pageable pageable);

}
