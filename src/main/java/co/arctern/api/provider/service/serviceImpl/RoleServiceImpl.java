package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.RoleDao;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.dto.request.RoleRequestDto;
import co.arctern.api.provider.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    public Role createRole(RoleRequestDto dto) {
        Role role = new Role();
        role.setIsActive(true);
        role.setRole(dto.getRole());
        role.setDescription(dto.getDescription());
        return roleDao.save(role);
    }

    public List<Role> fetchRoles(List<Long> roleIds) {
        return roleIds.stream().map(a -> roleDao.findById(a).orElseThrow(() ->
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role id.");
        })).collect(Collectors.toList());

    }
}
