package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.PaymentStateFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStateFlowRepository extends PagingAndSortingRepository<PaymentStateFlow, Long> {
}
