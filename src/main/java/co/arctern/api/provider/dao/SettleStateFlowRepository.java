package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.SettleStateFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * SettleStateFlow entity repository layer
 */
@Repository
public interface SettleStateFlowRepository extends PagingAndSortingRepository<SettleStateFlow, Long> {
}
