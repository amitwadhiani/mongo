package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface AreaService extends MessageUtil {

    public void setAreasToUser(User user, List<Long> areaIds);
}
