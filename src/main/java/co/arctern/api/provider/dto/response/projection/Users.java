package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.constant.Gender;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserArea;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * user entity default response body.
 */
@Projection(types = {User.class})
public interface Users {

    Long getId();

    String getName();

    String getUsername();

    String getEmail();

    String getPhone();

    Boolean getIsLoggedIn();

    Gender getGender();

    Integer getAge();

    @Value("#{@userAreaService.fetchAreasForUser(target.getUserAreas())}")
    List<Areas> getAreas();


}
