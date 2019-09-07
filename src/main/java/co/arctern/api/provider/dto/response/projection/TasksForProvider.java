package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Task;
import org.springframework.data.rest.core.config.Projection;

/**
 * tasks of a provider response body.
 */
@Projection(types = {Task.class})
public interface TasksForProvider {

    Long getId();
}
