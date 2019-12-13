package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.LoginFlow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * LoginFlow entity repository layer
 */
@Repository
public interface LoginFlowDao extends PagingAndSortingRepository<LoginFlow, Long> {
}
