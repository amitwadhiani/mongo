package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskFlowState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskStateFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface TaskStateFlowService extends MessageUtil {

    public TaskStateFlow createFlow(Task task, TaskFlowState state, Long userId);

}
