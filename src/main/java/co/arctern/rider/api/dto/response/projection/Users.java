package co.arctern.rider.api.dto.response.projection;

import co.arctern.rider.api.domain.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {User.class})
public interface Users {


}
