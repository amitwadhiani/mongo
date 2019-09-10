package co.arctern.api.provider.controller;

import co.arctern.api.provider.domain.Address;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@BasePathAwareController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @CrossOrigin
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Address> postAddress(@RequestBody TaskAssignDto dto) {
        return ResponseEntity.ok(addressService.saveAddress(dto));
    }
}
