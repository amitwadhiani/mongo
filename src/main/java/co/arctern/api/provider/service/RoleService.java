package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.dto.request.RoleRequestDto;
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
     * fetch roles based on roleIds.
     * @param roleIds
     * @return
     */
    public List<Role> fetchRoles(List<Long> roleIds);
}
