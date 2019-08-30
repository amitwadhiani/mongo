package co.arctern.rider.api.dao;

import co.arctern.rider.api.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AreaDao extends JpaRepository<Area, Long> {
}
