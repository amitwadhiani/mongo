package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;

public interface TaskService {

    public static final StringBuilder SUCCESS_MESSAGE = new StringBuilder("Success");

    public Task fetchTask(Long taskId);

    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto);

    public StringBuilder reassignTask(Long taskId, Long userId);
}
