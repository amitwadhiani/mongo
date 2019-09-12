package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskStateFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface TaskStateFlowService extends MessageUtil {

    /**
     * create flow for a task.
     * @param task
     * @param state
     * @param userId
     * @return
     */
    public TaskStateFlow createFlow(Task task, TaskEventFlowState state, Long userId);

}
