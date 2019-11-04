package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Cluster;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(types = {Cluster.class})
public interface Clusters {

    Long getId();

    String getName();

    Boolean getIsActive();

    List<Areas> getAreas();
}
