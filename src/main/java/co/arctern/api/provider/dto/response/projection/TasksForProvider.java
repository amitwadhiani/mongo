package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.domain.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;

/**
 * tasks of a provider response body.
 */
@Projection(types = {Task.class})
public interface TasksForProvider {

    Long getId();

    TaskState getState();

    String getCode();

    String getSource();

    @Value("#{@}")
    Timestamp getExpectedArrivalTime();

    Timestamp getCreatedAt();

    TaskType getType();

    Boolean getCancellationRequested();

    String getPatientName();

    String getPatientPhone();

    Boolean getIsPrepaid();

    Addresses getSourceAddress();

    Addresses getDestinationAddress();

    @Value("#{@userTaskServiceImpl.findActiveUserFromUserTask(target.getId())}")
    Users getUser();


}
