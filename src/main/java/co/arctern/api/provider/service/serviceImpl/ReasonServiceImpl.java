package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.ReasonDao;
import co.arctern.api.provider.dao.TaskReasonDao;
import co.arctern.api.provider.domain.Reason;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskReason;
import co.arctern.api.provider.dto.response.projection.Reasons;
import co.arctern.api.provider.service.ReasonService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReasonServiceImpl implements ReasonService {

    private final ReasonDao reasonDao;
    private final ProjectionFactory projectionFactory;
    private final TaskReasonDao taskReasonDao;

    @Autowired
    public ReasonServiceImpl(ReasonDao reasonDao,
                             ProjectionFactory projectionFactory,
                             TaskReasonDao taskReasonDao) {
        this.reasonDao = reasonDao;
        this.projectionFactory = projectionFactory;
        this.taskReasonDao = taskReasonDao;
    }

    @Override
    public StringBuilder create(List<String> reasons) {
        List<Reason> reasonsToSave = new ArrayList<>();
        reasons.stream().forEach(reason -> {
            Reason reasonEntity = new Reason();
            reasonEntity.setReason(reason);
            reasonEntity.setIsActive(true);
            reasonsToSave.add(reasonEntity);
        });
        reasonDao.saveAll(reasonsToSave);
        return SUCCESS_MESSAGE;
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

    @Override
    public List<Reasons> fetchAll() {
        return Lists.newArrayList(reasonDao.findAll()).stream().map(a -> projectionFactory.createProjection(Reasons.class, a))
                .collect(Collectors.toList());
    }
}
