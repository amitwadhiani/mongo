package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.request.AreaRequestDto;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService extends MessageUtil {

    /**
     * assign areas to user.
     *
     * @param user
     * @param areaIds
     */
    public void setAreasToUser(User user, List<Long> areaIds);

    /**
     * create areas.
     *
     * @param dtos
     * @return
     */
    public StringBuilder createAreas(List<AreaRequestDto> dtos);

    public Page<Areas> fetchAreas(Pageable pageable);
}
