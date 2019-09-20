package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.constant.OfferingType;
import co.arctern.api.provider.domain.Offering;
import org.springframework.data.rest.core.config.Projection;

/**
 * offerings response body.
 */
@Projection(types = {Offering.class})
public interface Offerings {

    Long getId();

    String getDescription();

    OfferingType getType();

    Boolean getIsActive();
}
