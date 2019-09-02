package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskStateFlowState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.util.MessageUtil;

public interface TaskService extends MessageUtil {

    public Task fetchTask(Long taskId);

    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto);

    public StringBuilder reassignTask(Long taskId, Long userId);

    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId);

    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId);

    public StringBuilder acceptOrRejectAssignedTask(Long taskId, TaskStateFlowState state);
}
