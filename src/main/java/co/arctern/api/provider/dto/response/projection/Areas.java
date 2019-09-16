package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Area;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {Area.class})
public interface Areas {

    Long getId();

    String getPinCode();

    String getCluster();
}
