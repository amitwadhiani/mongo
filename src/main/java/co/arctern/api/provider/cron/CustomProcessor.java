package co.arctern.api.provider.cron;

import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.UserTask;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TaskStateFlowService;
import co.arctern.api.provider.service.UserTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomProcessor {

    private final UserTaskService userTaskService;
    private final TaskService taskService;
    private final TaskStateFlowService taskStateFlowService;

    @Autowired
    public CustomProcessor(UserTaskService userTaskService, TaskStateFlowService taskStateFlowService, TaskService taskService) {
        this.userTaskService = userTaskService;
        this.taskService = taskService;
        this.taskStateFlowService = taskStateFlowService;
    }

    @Scheduled(cron = "0 */20 6-23 * * *")
    @Async("threadPoolTaskExecutor")
    public void processUnattendedTasks() {
        List<UserTask> userTasks = userTaskService.fetchUserTasksForCron();
        log.info("Thread running for cron with -> " + userTasks.size() + " items");
        List<Task> tasksToSave = new ArrayList<>();
        userTasks.stream().forEach(a -> {
            Long userId = a.getUser().getId();
            Task task = a.getTask();
            userTaskService.markInactive(task);
            taskStateFlowService.createFlow(task, TaskEventFlowState.TIMED_OUT, userId);
            taskStateFlowService.createFlow(task, TaskEventFlowState.OPEN, userId);
            task.setState(TaskState.OPEN);
            tasksToSave.add(task);
        });
        taskService.saveAll(tasksToSave);
    }
}