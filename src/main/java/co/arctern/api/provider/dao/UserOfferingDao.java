package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserOffering;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@PreAuthorize("isAuthenticated()")
public interface UserOfferingDao extends PagingAndSortingRepository<UserOffering, Long> {
}
