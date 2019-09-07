package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskFlowState;
import co.arctern.api.provider.domain.TaskEvent;
import co.arctern.api.provider.domain.TaskEventFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface TaskEventFlowService extends MessageUtil {

    /**
     * flow creation for a particular taskEvent.
     *
     * @param taskEvent
     * @param state
     * @param userId
     * @return
     */
    public TaskEventFlow createFlow(TaskEvent taskEvent, TaskFlowState state, Long userId);
}
