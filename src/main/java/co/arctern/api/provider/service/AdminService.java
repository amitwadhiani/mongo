package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService extends MessageUtil {

    /**
     * fetch providers through areaIds
     *
     * @param areaIds
     * @return
     */
    PaginatedResponse fetchProvidersByArea(List<Long> areaIds, Pageable pageable);
}
