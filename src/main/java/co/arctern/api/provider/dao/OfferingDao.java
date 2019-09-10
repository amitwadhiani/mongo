package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Offering;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferingDao extends PagingAndSortingRepository<Offering, Long> {
}
