package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.response.projection.Reasons;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface ReasonService extends MessageUtil {

    /**
     * create new reasons.
     * @param reasons
     * @return
     */
    StringBuilder create(List<String> reasons);

    /**
     * assign reasons with a task.
     * @param task
     * @param reasonIds
     * @return
     */
    StringBuilder assignReasons(Task task, List<Long> reasonIds);

    /**
     * fetch all reasons for declining the task/requesting cancellation.
     * @return
     */
    List<Reasons> fetchAll();
}
