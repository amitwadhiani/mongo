package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenericServiceImpl implements GenericService {

    private final TaskDao taskDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public GenericServiceImpl(TaskDao taskDao,
                              ProjectionFactory projectionFactory) {
        this.taskDao = taskDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public Double fetchUserOwedAmount(Long userId) {
        List<Task> tasks = taskDao.fetchTasks(userId, TaskState.COMPLETED);
        List<Payments> payments = new ArrayList<>();
        tasks.stream().forEach(a -> {
            payments.addAll(a.getPayments().stream().filter(b -> b.getSettleState().equals(SettleState.PAYMENT_RECEIVED))
                    .map(c -> projectionFactory.createProjection(Payments.class, c)).collect(Collectors.toList()));
        });
        return Math.round(payments.stream().mapToDouble(a -> a.getAmount()).sum()) * 100 / 100D;
    }
}
