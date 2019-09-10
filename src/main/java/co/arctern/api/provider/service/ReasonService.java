package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.response.projection.Reasons;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface ReasonService extends MessageUtil {

    StringBuilder create(List<String> reasons);

    StringBuilder assignReasons(Task task, List<Long> reasonIds);

    List<Reasons> fetchAll();
}
