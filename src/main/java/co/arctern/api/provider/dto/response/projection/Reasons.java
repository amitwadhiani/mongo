package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Reason;
import org.springframework.data.rest.core.config.Projection;

@Projection(types={Reason.class})
public interface Reasons {

    Long getId();

    String getReason();
}
