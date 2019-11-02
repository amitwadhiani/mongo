package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.service.GenericService;
import co.arctern.api.provider.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericServiceImpl implements GenericService {

    private final TaskService taskService;

    @Autowired
    public GenericServiceImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Double fetchUserOwedAmount(Long userId) {
        return taskService.fetchUserOwedAmount(userId);
    }
}
