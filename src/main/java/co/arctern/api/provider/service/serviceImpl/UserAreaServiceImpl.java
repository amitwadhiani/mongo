package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserAreaDao;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.service.UserAreaService;
import co.arctern.api.provider.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserAreaServiceImpl implements UserAreaService {

    @Autowired
    private UserAreaDao userAreaDao;

    @Override
    public PaginatedResponse fetchUsersByArea(List<Long> areaIds, Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userAreaDao.findByAreaIdInAndIsActiveTrue(areaIds, pageable).map(
                a -> a.getUser()), pageable);
    }

    @Override
    public  Map<Long, List<Long>> fetchUsersByArea(Pageable pageable) {
       return userAreaDao.findByIsActiveTrue(pageable).stream()
                .collect(Collectors.groupingBy(a -> a.getArea().getId(), Collectors.mapping(a -> a.getUser().getId(), Collectors.toList())));
    }
}
