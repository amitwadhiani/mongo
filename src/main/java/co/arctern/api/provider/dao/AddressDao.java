package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Address;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AddressDao extends PagingAndSortingRepository<Address, Long> {
}
