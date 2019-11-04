package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Address;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Address entity repository layer
 */
@Repository
public interface AddressDao extends PagingAndSortingRepository<Address, Long> {

    List<Address> findByIsSourceAddressTrue();
}
