package co.arctern.api.provider.cron;

import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TaskStateFlowService;
import co.arctern.api.provider.service.UserTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * cron job processor for marking unattended assigned tasks as open automatically.
 */
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

    /**
     * scheduled to run after every 20 minutes
     */
//    @Scheduled(cron = "0 */20 * * * *")
//    @Async("threadPoolTaskExecutor")
//    public void processUnattendedTasks() {
//        List<UserTask> userTasks = userTaskService.fetchUserTasksForCron();
//        log.info("Thread running for cron with -> " + userTasks.size() + " items");
//        List<Task> tasksToSave = new ArrayList<>();
//        userTasks.stream().forEach(a -> {
//            Long userId = a.getUser().getId();
//            Task task = a.getTask();
//            userTaskService.markInactive(task);
//            taskStateFlowService.createFlow(task, TaskStateFlowState.TIMED_OUT, userId);
//            taskStateFlowService.createFlow(task, TaskStateFlowState.OPEN, userId);
//            task.setState(TaskState.OPEN);
//            tasksToSave.add(task);
//        });
//        taskService.saveAll(tasksToSave);
//    }
}