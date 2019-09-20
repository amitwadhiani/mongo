package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * UserTask entity repository layer
 */
@Repository
public interface UserTaskDao extends PagingAndSortingRepository<UserTask, Long> {

    /**
     * find active task for user .
     *
     * @param userId
     * @param state
     * @return
     */
    Page<UserTask> findByIsActiveTrueAndUserIdAndTaskStateOrderByTaskCreatedAtDesc(Long userId, TaskState state, Pageable pageable);

    /**
     * find user_task for task_id.
     *
     * @param taskId
     * @return
     */
    UserTask findByIsActiveTrueAndTaskId(Long taskId);

    /**
     * fetch users filtered by task state and within a time range.
     *
     * @param userId
     * @param state
     * @param start
     * @param end
     * @return
     */
    List<UserTask> findByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(Long userId, TaskState state,
                                                                                                                   Timestamp start, Timestamp end);

    /**
     * fetch users through task state and greater than a time.
     *
     * @param userId
     * @param state
     * @param start
     * @return
     */
    List<UserTask> findByIsActiveTrueAndUserIdAndTaskStateAndCreatedAtGreaterThanEqual(Long userId, TaskState state, Timestamp start);

    /**
     * fetch count of tasks for a user filtered by task creation time.
     *
     * @param userId
     * @param state
     * @param start
     * @param end
     * @return
     */
    Long countByIsActiveTrueAndUserIdAndTaskStateAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(
            Long userId, TaskState state, Timestamp start, Timestamp end);

    /**
     * fetch tasks filtered by task states and within a time range.
     *
     * @param states
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<UserTask> findByIsActiveTrueAndTaskStateInAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(TaskState[] states, Timestamp start,
                                                                                                            Timestamp end, Pageable pageable);

}
