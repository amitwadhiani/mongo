package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskStateFlow;

public interface TaskStateFlowService {

    public TaskStateFlow createFlow(Task task, TaskState state);
}
