package co.arctern.api.provider.service;

import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserOfferingService extends MessageUtil {

    public Map<Long, List<Long>> fetchUsersByOffering(Pageable pageable);
}
