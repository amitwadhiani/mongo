package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.LoginStateFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/**
 * LoginStateFlow entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface LoginStateFlowDao extends PagingAndSortingRepository<LoginStateFlow, Long> {
}
