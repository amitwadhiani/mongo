package co.arctern.api.provider.dao;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.domain.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
     * @param states
     * @return
     */
    Page<UserTask> findByIsActiveTrueAndUserIdAndTaskStateInOrderByTaskCreatedAtDesc(Long userId, TaskState[] states, Pageable pageable);

    Page<UserTask> findByIsActiveTrueAndUserIdAndTaskStateInAndTaskTypeOrderByTaskCreatedAtDesc(Long userId, TaskState[] states, TaskType type, Pageable pageable);

    Page<UserTask> findByIsActiveTrueAndUserIdAndTaskStateInAndTaskTypeAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThanOrderByTaskCreatedAtDesc(Long userId, TaskState[] states, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * find user_task for task_id.
     *
     * @param taskId
     * @return
     */
    UserTask findByIsActiveTrueAndTaskId(Long taskId);

    /**
     * fetch userTasks filtered by task state and within a time range.
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
     * fetch userTasks through task state and greater than a time.
     *
     * @param userId
     * @param state
     * @param start
     * @return
     */
    List<UserTask> findByIsActiveTrueAndUserIdAndTaskStateAndCreatedAtGreaterThanEqual(Long userId, TaskState state, Timestamp start);

    /**
     * fetch userTasks through userId and taskState.
     *
     * @param userId
     * @param state
     * @return
     */
    List<UserTask> findByIsActiveTrueAndUserIdAndTaskState(Long userId, TaskState state);

    Page<UserTask> findByIsActiveTrueAndUserIdInAndTaskType(List<Long> userIds, TaskType type, Pageable pageable);

    Page<UserTask> findByIsActiveTrueAndUserIdInAndTaskTypeAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(List<Long> userIds, TaskType type, Timestamp start, Timestamp end, Pageable pageable);

    /**
     * fetch count of userTasks for a user filtered by task creation time.
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
     * fetch userTasks filtered by task states and within a time range.
     *
     * @param states
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<UserTask> findByIsActiveTrueAndTaskStateInAndTaskCreatedAtGreaterThanEqualAndTaskCreatedAtLessThan(TaskState[] states, Timestamp start,
                                                                                                            Timestamp end, Pageable pageable);

    /**
     * to fetch userTasks for cron.
     *
     * @param createdAt
     * @param state
     * @return
     */
    @Query("FROM UserTask ut " +
            "JOIN FETCH ut.task task " +
            "JOIN FETCH ut.user user " +
            "WHERE ut.isActive = 1 " +
            "AND ut.createdAt <= (:createdAt) " +
            "AND task.isActive = 1 " +
            "AND task.state = :state ")
    List<UserTask> fetchUserTasksForCron(@Param("createdAt") Timestamp createdAt, @Param("state") TaskState state);
}
