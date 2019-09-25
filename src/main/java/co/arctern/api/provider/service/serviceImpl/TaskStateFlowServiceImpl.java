package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskStateFlowState;
import co.arctern.api.provider.dao.TaskStateFlowDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskStateFlow;
import co.arctern.api.provider.service.TaskStateFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskStateFlowServiceImpl implements TaskStateFlowService {

    private final TaskStateFlowDao taskStateFlowDao;

    @Autowired
    public TaskStateFlowServiceImpl(TaskStateFlowDao taskStateFlowDao) {
        this.taskStateFlowDao = taskStateFlowDao;
    }

    @Override
    public TaskStateFlow createFlow(Task task, TaskStateFlowState state, Long userId) {
        TaskStateFlow taskStateFlow = new TaskStateFlow();
        taskStateFlow.setTask(task);
        taskStateFlow.setState(state);
        taskStateFlow.setUserId(userId);
        return taskStateFlowDao.save(taskStateFlow);
    }
}
