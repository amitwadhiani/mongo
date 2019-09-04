package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * UserTask entity repository layer
 */
@RepositoryRestResource(exported = false)
public interface UserTaskDao extends PagingAndSortingRepository<UserTask, Long> {

    /**
     * find active task for user .
     *
     * @param userId
     * @param state
     * @return
     */
    Page<UserTask> findByIsActiveTrueAndUserIdAndTaskStateOrderByCreatedAtDesc(Long userId, TaskState state, Pageable pageable);

    /**
     * find user_task for task_id.
     *
     * @param taskId
     * @return
     */
    UserTask findByIsActiveTrueAndTaskId(Long taskId);
}
