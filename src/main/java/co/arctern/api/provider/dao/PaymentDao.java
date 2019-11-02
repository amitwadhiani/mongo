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

    List<Payment> findBySettleState(SettleState settleState);

    List<Payment> findBySettleStateAndPaidBy(SettleState settleState, Long paidBy);
}
