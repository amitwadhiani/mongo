package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.dao.RoleDao;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.dto.request.RoleRequestDto;
import co.arctern.api.provider.dto.response.projection.Roles;
import co.arctern.api.provider.service.RoleService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao,
                           ProjectionFactory projectionFactory) {
        this.roleDao = roleDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    @Transactional
    public Role createRole(RoleRequestDto dto) {
        Role role = new Role();
        role.setIsActive(true);
        role.setRole(dto.getRole());
        role.setDescription(dto.getDescription());
        return roleDao.save(role);
    }

    @Override
    public List<Roles> fetchRoles() {
        return Lists.newArrayList(roleDao.findAll()).stream()
                .map(role -> projectionFactory.createProjection(Roles.class, role))
                .collect(Collectors.toList());

    }

    @Override
    public Roles fetchRoleById(Long id) {
        return projectionFactory.createProjection(Roles.class, roleDao.findById(id).orElseThrow(() ->
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ROLE_ID_MESSAGE.toString());
        }));
    }

    @Override
    public List<Role> fetchRoleById(List<Long> ids) {
        return roleDao.findByIdIn(ids);
    }

}
