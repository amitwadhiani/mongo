package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskStateFlowState;
import co.arctern.api.provider.dao.ReasonDao;
import co.arctern.api.provider.dao.TaskReasonDao;
import co.arctern.api.provider.domain.Reason;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.TaskReason;
import co.arctern.api.provider.dto.request.ReasonEditBody;
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
            taskReason.setIsActive(true);
            taskReason.setState(TaskStateFlowState.CANCELLED);
            taskReasons.add(taskReason);
        });
        taskReasonDao.saveAll(taskReasons);
        return SUCCESS_MESSAGE;
    }

    @Override
    public List<Reasons> fetchAll() {
        return Lists.newArrayList(reasonDao.findAll()).stream().map(reason -> projectionFactory.createProjection(Reasons.class, reason))
                .collect(Collectors.toList());
    }

    @Override
    public StringBuilder edit(List<ReasonEditBody> bodies) {
        List<Reason> reasonsToSave = new ArrayList<>();
        bodies.stream().forEach(a -> {
            Reason reason = reasonDao.findById(a.getReasonId()).get();
            reason.setIsActive(a.getIsActive());
            reasonsToSave.add(reason);
        });
        reasonDao.saveAll(reasonsToSave);
        return SUCCESS_MESSAGE;
    }
}
