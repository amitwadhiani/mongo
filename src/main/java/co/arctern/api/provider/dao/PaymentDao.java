package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.domain.Payment;
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
}
