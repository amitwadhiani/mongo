package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Reason;
import org.springframework.data.rest.core.config.Projection;

/**
 * reasons response body .
 */
@Projection(types={Reason.class})
public interface Reasons {

    Long getId();

    String getReason();

    Boolean getIsActive();

}
