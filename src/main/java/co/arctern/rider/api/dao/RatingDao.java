package co.arctern.rider.api.dao;

import co.arctern.rider.api.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface RatingDao extends JpaRepository<Rating, Long> {
}
