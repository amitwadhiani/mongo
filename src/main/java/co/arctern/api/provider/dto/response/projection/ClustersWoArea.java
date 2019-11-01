package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.Cluster;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {Cluster.class})
public interface ClustersWoArea {

    Long getId();

    String getName();

    Boolean getIsActive();
}
