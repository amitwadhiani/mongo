package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskEventState;
import co.arctern.api.provider.dao.TaskEventDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskEvent;
import co.arctern.api.provider.service.TaskEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskEventServiceImpl implements TaskEventService {

    @Autowired
    TaskEventDao taskEventDao;

    @Override
    public TaskEvent createFlow(Task task, TaskEventState state, Long userId) {
        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setTask(task);
        taskEvent.setState(state);
        taskEvent.setUserId(userId);
        return taskEventDao.save(taskEvent);
    }
}
