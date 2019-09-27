package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.dto.request.RoleRequestDto;
import co.arctern.api.provider.dto.response.projection.Roles;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface RoleService extends MessageUtil {

    /**
     * role creation.
     * @param dto
     * @return
     */
    public Role createRole(RoleRequestDto dto);

    /**
     * fetch all roles.
     * @return
     */
    public List<Roles> fetchRoles();

    /**
     * fetch role through id.
     * @param id
     * @return
     */
    public Roles fetchRoleById(Long id);


}
