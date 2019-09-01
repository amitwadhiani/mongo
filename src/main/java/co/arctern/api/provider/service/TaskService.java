package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Task;

public interface TaskService {

    public Task fetchTask(Long taskId);

}
