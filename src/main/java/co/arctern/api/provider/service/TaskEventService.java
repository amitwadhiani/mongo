package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskEventState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskEvent;
import co.arctern.api.provider.util.MessageUtil;

public interface TaskEventService extends MessageUtil {

    /**
     * flow creation for a particular task.
     *
     * @param task
     * @param state
     * @param userId
     * @return
     */
    public TaskEvent createFlow(Task task, TaskEventState state, Long userId);
}
