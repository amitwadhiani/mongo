package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/**
 * Rating entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface RatingDao extends PagingAndSortingRepository<Rating, Long> {
}
