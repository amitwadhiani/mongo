package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Address;
import org.springframework.data.rest.core.config.Projection;

/**
 * addresses response body.
 */
@Projection(types = {Address.class})
public interface Addresses {

    Long getId();

    String getName();

    String getLine();

    String getPinCode();

    Areas getArea();

    String getCity();

    Boolean getIsSourceAddress();

    String getLocality();

    String getHouseNumber();

    String getState();

    Double getLatitude();

    Double getLongitude();

    String getLandmark();

    String getSpecialInstructions();

    String getFullAddress();

}
