package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.UserTask;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserTaskDao extends PagingAndSortingRepository<UserTask, Long> {
}
