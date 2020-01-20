package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.UserCluster;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {UserCluster.class})
public interface UserClusters {

    public Long getId();

    public ClustersWoArea getCluster();
}
