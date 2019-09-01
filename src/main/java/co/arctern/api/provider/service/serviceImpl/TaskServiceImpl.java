package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TaskStateFlowService;
import co.arctern.api.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    TaskStateFlowService taskStateFlowService;

    @Autowired
    private UserService userService;

    @Override
    public Task fetchTask(Long taskId) {
        return taskDao.findById(taskId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid taskId");
        });
    }

    @Override
    public StringBuilder createTaskAndAssignUser(TaskAssignDto dto) {
        Task task = new Task();
        task.setUser(userService.fetchUser(dto.getUserId()));
        task.setAmount(dto.getAmount());
        task.setPaymentState(dto.getPaymentState());
        task.setDiagnosticOrderId(dto.getDiagnosticOrderId());
        taskStateFlowService.createFlow(taskDao.save(task), TaskState.ASSIGNED);
        taskStateFlowService.createFlow(taskDao.save(task), TaskState.ACCEPTED);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder reassignTask(Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        task.setUser(userService.fetchUser(userId));
        task.setIsActive(true);
        task.setCancellationRequested(false);
        taskStateFlowService.createFlow(taskDao.save(task), TaskState.REASSIGNED);
        taskStateFlowService.createFlow(taskDao.save(task), TaskState.ACCEPTED);
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder cancelTask(Boolean isCancelled, Long taskId, Long userId) {
        Task task = this.fetchTask(taskId);
        if (isCancelled) {
            /**
             *  cancel
             */
            task.setIsActive(false);
            taskStateFlowService.createFlow(task, TaskState.CANCELLED);
        } else {
            /**
             * reassign
             */
            task.setUser(userService.fetchUser(userId));
            taskStateFlowService.createFlow(task, TaskState.REASSIGNED);
            taskStateFlowService.createFlow(task, TaskState.ACCEPTED);
        }
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder requestCancellation(Boolean cancelRequest, Long taskId) {
        Task task = this.fetchTask(taskId);
        task.setCancellationRequested(cancelRequest);
        taskDao.save(task);
        return SUCCESS_MESSAGE;
    }

}
