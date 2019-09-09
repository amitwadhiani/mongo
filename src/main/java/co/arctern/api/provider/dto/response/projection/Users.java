package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.constant.Gender;
import co.arctern.api.provider.domain.User;
import org.springframework.data.rest.core.config.Projection;

/**
 * user entity default response body.
 */
@Projection(types = {User.class})
public interface Users {

    Long getId();

    String getName();

    String getUsername();

    String getEmail();

    Gender getGender();

    Integer getAge();

}
