package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Role;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {Role.class})
public interface Roles {

     Long getId();

    String getRole();

    String getDescription();

    Boolean getIsActive();
}
