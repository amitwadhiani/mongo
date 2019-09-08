package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.ReasonDao;
import co.arctern.api.provider.dao.TaskReasonDao;
import co.arctern.api.provider.domain.Reason;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskReason;
import co.arctern.api.provider.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReasonServiceImpl implements ReasonService {

    @Autowired
    ReasonDao reasonDao;

    @Autowired
    TaskReasonDao taskReasonDao;

    @Override
    public Reason create(String reason) {
        Reason reasonEntity = new Reason();
        reasonEntity.setReason(reason);
        reasonEntity.setIsActive(true);
        return reasonDao.save(reasonEntity);
    }

    @Override
    public StringBuilder assignReasons(Task task, List<Long> reasonIds) {
        List<TaskReason> taskReasons = new ArrayList<>();
        reasonDao.findByIdIn(reasonIds).stream().forEach(reason ->
        {
            TaskReason taskReason = new TaskReason();
            taskReason.setTask(task);
            taskReason.setReason(reason);
            taskReasons.add(taskReason);
        });
        taskReasonDao.saveAll(taskReasons);
        return SUCCESS_MESSAGE;
    }
}
