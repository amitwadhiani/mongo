package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Rating;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Rating entity repository layer
 */
@Repository
public interface RatingDao extends PagingAndSortingRepository<Rating, Long> {
}
