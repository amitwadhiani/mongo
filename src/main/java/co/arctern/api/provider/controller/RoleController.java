package co.arctern.api.provider.controller;

import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.dto.request.RoleRequestDto;
import co.arctern.api.provider.dto.response.projection.Roles;
import co.arctern.api.provider.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * apis for role creation for Admin.
 */
@BasePathAwareController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * create new roles api.
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Role> createNewRole(@RequestBody RoleRequestDto dto) {
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    /**
     * fetch all roles api.
     * @return
     */
    @PostMapping("/fetch/all")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Roles>> fetchAllRoles() {
        return ResponseEntity.ok(roleService.fetchRoles());
    }

    /**
     * fetch role by id api.
     * @param id
     * @return
     */
    @PostMapping("/fetch/{id}")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Roles> fetchRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.fetchRoleById(id));
    }

}
