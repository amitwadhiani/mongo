package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.TasksForRiderResponse;
import co.arctern.api.provider.util.MessageUtil;

public interface RiderService extends MessageUtil {

    TasksForRiderResponse fetchTasksForRider(Long userId);

}
