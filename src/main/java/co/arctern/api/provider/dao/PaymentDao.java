package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Payment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * payment entity repository layer.
 */
@Repository
public interface PaymentDao extends PagingAndSortingRepository<Payment, Long> {
}
