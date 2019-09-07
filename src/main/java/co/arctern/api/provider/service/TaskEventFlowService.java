package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskEvent;
import co.arctern.api.provider.domain.TaskEventFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface TaskEventFlowService extends MessageUtil {

    /**
     * flow creation for a particular taskEvent.
     *
     * @param task
     * @param state
     * @param userId
     * @return
     */
    public TaskEventFlow createFlow(TaskEvent task, TaskEventFlowState state, Long userId);
}
