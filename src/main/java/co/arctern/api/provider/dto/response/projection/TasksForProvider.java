package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.domain.Task;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;

/**
 * tasks of a provider response body.
 */
@Projection(types = {Task.class})
public interface TasksForProvider {

    Long getId();

    TaskState getState();

    Timestamp getExpectedArrivalTime();

    Timestamp getCreatedAt();

    TaskType getType();

    String getPatientName();

    String getPatientPhone();

    Boolean getIsPrepaid();


}
