package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.dto.response.projection.Offerings;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserOfferingService extends MessageUtil {

    public Map<Object, List<Users>> fetchUsersByOffering(Pageable pageable);
}
