package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Area;
import org.springframework.data.rest.core.config.Projection;

/**
 * areas response body.
 */
@Projection(types = {Area.class})
public interface Areas {

    Long getId();

    String getPinCode();

    Clusters getCluster();
}
