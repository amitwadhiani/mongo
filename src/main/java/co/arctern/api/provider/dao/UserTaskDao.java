package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.UserTask;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserTaskDao extends PagingAndSortingRepository<UserTask, Long> {

    List<UserTask> findByIsActiveTrueAndUserIdAndTaskState(Long userId, TaskState state);

    UserTask findByIsActiveTrueAndTaskId(Long taskId);
}
