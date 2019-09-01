package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;


/**
 * Rating entity repository layer
 */
@RepositoryRestResource(exported = false)
@PreAuthorize("isAuthenticated()")
public interface RatingDao extends PagingAndSortingRepository<Rating, Long> {
}
