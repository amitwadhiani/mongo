package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserOfferingDao;
import co.arctern.api.provider.service.UserOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserOfferingServiceImpl implements UserOfferingService {

    @Autowired
    UserOfferingDao userOfferingDao;

    @Override
    public Map<Long, List<Long>> fetchUsersByOffering(Pageable pageable) {
        return userOfferingDao.findByIsActiveTrue(pageable).stream()
                .collect(Collectors.groupingBy(a -> a.getOffering().getId(), Collectors.mapping(a -> a.getUser().getId(), Collectors.toList())));
    }
}
