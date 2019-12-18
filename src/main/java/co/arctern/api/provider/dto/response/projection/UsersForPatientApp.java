package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.User;
import org.springframework.data.rest.core.config.Projection;

/**
 * user entity custom response for patient app
 */
@Projection(types = {User.class})
public interface UsersForPatientApp {
    String getName();
    String getPhone();
}
