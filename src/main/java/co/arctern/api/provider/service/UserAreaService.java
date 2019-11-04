package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.UserArea;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.ClustersWoArea;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserAreaService extends MessageUtil {

    /**
     * @param areaIds
     * @param pageable
     * @return
     */
    PaginatedResponse fetchUsersByArea(List<Long> areaIds, Pageable pageable);

    /**
     * fetch areas associated with the user.
     *
     * @param userAreas
     * @return
     */
    public List<ClustersWoArea> fetchAreasForUser(List<UserArea> userAreas);

}
