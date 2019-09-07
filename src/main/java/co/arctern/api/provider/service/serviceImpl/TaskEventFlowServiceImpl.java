package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskFlowState;
import co.arctern.api.provider.dao.TaskEventFlowDao;
import co.arctern.api.provider.domain.TaskEvent;
import co.arctern.api.provider.domain.TaskEventFlow;
import co.arctern.api.provider.service.TaskEventFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskEventFlowServiceImpl implements TaskEventFlowService {

    @Autowired
    TaskEventFlowDao taskEventFlowDao;

    @Override
    public TaskEventFlow createFlow(TaskEvent taskEvent, TaskFlowState state, Long userId) {
        TaskEventFlow taskEventFlow = new TaskEventFlow();
        taskEventFlow.setTaskEvent(taskEvent);
        taskEventFlow.setState(state);
        taskEventFlow.setUserId(userId);
        return taskEventFlowDao.save(taskEventFlow);
    }
}
