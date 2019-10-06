package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Role;
import org.springframework.data.rest.core.config.Projection;

/**
 * roles response body for role Entity.
 */
@Projection(types = {Role.class})
public interface Roles {

    Long getId();

    String getRole();

    String getDescription();

    Boolean getIsActive();
}
