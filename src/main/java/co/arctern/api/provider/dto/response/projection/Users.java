package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.User;
import org.springframework.data.rest.core.config.Projection;

/**
 * user entity default response body.
 */
@Projection(types = {User.class})
public interface Users {


}
