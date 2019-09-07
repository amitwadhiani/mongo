package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.request.AreaRequestDto;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface AreaService extends MessageUtil {

    /**
     * assign areas to user.
     *
     * @param user
     * @param areaIds
     */
    public void setAreasToUser(User user, List<Long> areaIds);

    public StringBuilder createAreas(List<AreaRequestDto> dtos);
}
