package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.service.AdminService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TokenService;
import co.arctern.api.provider.service.UserAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserAreaService userAreaService;

    @Override
    public PaginatedResponse fetchProvidersByArea(List<Long> areaIds, Pageable pageable) {
        return userAreaService.fetchUsersByArea(areaIds, pageable);
    }

}
