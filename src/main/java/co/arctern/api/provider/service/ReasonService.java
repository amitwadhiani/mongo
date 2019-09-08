package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Reason;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface ReasonService extends MessageUtil {

    Reason create(String reason);

    StringBuilder assignReasons(Task task, List<Long> reasonIds);
}
