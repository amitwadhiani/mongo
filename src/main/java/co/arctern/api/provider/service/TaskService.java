package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskEventState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.TasksForRider;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface TaskService extends MessageUtil {

    public Task fetchTask(Long taskId);

    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto);

    public StringBuilder reassignTask(Long taskId, Long userId);

    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId);

    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId);

    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskEventState state);

    public List<TasksForRider> fetchCompletedTasksForUser(Long userId);

    public List<TasksForRider> fetchAssignedTasksForUser(Long userId);

    public List<TasksForRider> fetchCancelledTasksForUser(Long userId);

}
