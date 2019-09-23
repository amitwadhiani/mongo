package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.dao.TaskEventFlowDao;
import co.arctern.api.provider.domain.TaskEvent;
import co.arctern.api.provider.domain.TaskEventFlow;
import co.arctern.api.provider.service.TaskEventFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskEventFlowServiceImpl implements TaskEventFlowService {

    private final TaskEventFlowDao taskEventFlowDao;

    @Autowired
    public TaskEventFlowServiceImpl(TaskEventFlowDao taskEventFlowDao) {
        this.taskEventFlowDao = taskEventFlowDao;
    }

    @Override
    public TaskEventFlow createFlow(TaskEvent taskEvent, TaskEventFlowState state, Long userId) {
        TaskEventFlow taskEventFlow = new TaskEventFlow();
        taskEventFlow.setTaskEvent(taskEvent);
        taskEventFlow.setState(state);
        taskEventFlow.setUserId(userId);
        return taskEventFlowDao.save(taskEventFlow);
    }
}
