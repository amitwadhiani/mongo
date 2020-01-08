package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.dto.response.projection.PaymentsForUser;
import co.arctern.api.provider.service.GenericService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenericServiceImpl implements GenericService {

    private final TaskDao taskDao;
    private final UserDao userDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public GenericServiceImpl(TaskDao taskDao,
                              UserDao userDao,
                              ProjectionFactory projectionFactory) {
        this.taskDao = taskDao;
        this.userDao = userDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public Double fetchUserOwedAmount(Long userId) {
        return Math.round(this.getReceivedPaymentsForUser(userId).stream().mapToDouble(a -> a.getAmount()).sum()) * 100 / 100D;
    }

    @Override
    public List<Payments> getReceivedPaymentsForUser(Long userId) {
        List<Task> tasks = taskDao.fetchTasks(userId, TaskState.COMPLETED);
        List<Payments> payments = new ArrayList<>();
        tasks.stream().forEach(a -> {
            payments.addAll(a.getPayments().stream().filter(b -> b.getSettleState().equals(SettleState.PAYMENT_RECEIVED))
                    .map(c -> projectionFactory.createProjection(Payments.class, c)).collect(Collectors.toList()));
        });
        return payments;
    }

    @Override
    public PaginatedResponse getPaymentsForUser(Long userId, Pageable pageable) {
        Page<Task> tasks = taskDao.fetchTasksForUser(userId, TaskState.COMPLETED, pageable);
        List<PaymentsForUser> payments = new ArrayList<>();
        tasks.stream().forEach(a -> {
            payments.addAll(a.getPayments().stream()
                    .map(c -> projectionFactory.createProjection(PaymentsForUser.class, c)).collect(Collectors.toList()));
        });
        return PaginationUtil.returnPaginatedBody(payments, pageable.getPageNumber(), pageable.getPageSize(), (int) tasks.getTotalElements());
    }

    @Override
    public PaginatedResponse fetchPaymentInfo(Long userId, Pageable pageable) {
        return this.getPaymentsForUser(userId, pageable);
    }

    @Override
    public User fetchUser(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE.toString()));
    }
}
