package co.arctern.api.provider.cron.processor;

import co.arctern.api.provider.constant.TaskEventFlowState;
import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TaskStateFlowService;
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

    private final TaskService taskService;
    private final TaskStateFlowService taskStateFlowService;

    @Autowired
    public CustomProcessor(TaskService taskService, TaskStateFlowService taskStateFlowService) {
        this.taskService = taskService;
        this.taskStateFlowService = taskStateFlowService;
    }

    @Scheduled(fixedDelay = 3600000)
    @Async("threadPoolTaskExecutor")
    public void processUnattendedTasks() {
        List<Task> tasks = taskService.fetchTasksForCron();
        log.info("Thread running for cron with -> " + tasks.size() + " items");
        List<Task> tasksToSave = new ArrayList<>();
        tasks.stream().forEach(a -> {
            Long userId = a.getUserTasks().stream().filter(b -> b.getIsActive()).findFirst().get().getUser().getId();
            taskStateFlowService.createFlow(a, TaskEventFlowState.TIMED_OUT, userId);
            taskStateFlowService.createFlow(a, TaskEventFlowState.OPEN, userId);
            a.setState(TaskState.OPEN);
            tasksToSave.add(a);
        });
        taskService.saveAll(tasksToSave);
    }
}