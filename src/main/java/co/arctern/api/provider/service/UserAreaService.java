package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserAreaService extends MessageUtil {

    /**
     * @param areaIds
     * @param pageable
     * @return
     */
    PaginatedResponse fetchUsersByArea(List<Long> areaIds, Pageable pageable);

    public Map<Long, List<Long>> fetchUsersByArea(Pageable pageable);

}
