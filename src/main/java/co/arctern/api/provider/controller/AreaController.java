package co.arctern.api.provider.controller;

import co.arctern.api.provider.dto.request.AreaRequestDto;
import co.arctern.api.provider.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@BasePathAwareController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    AreaService areaService;

    /**
     * create new area api.
     * @param dtos
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> createAreas(List<AreaRequestDto> dtos) {
        return ResponseEntity.ok(areaService.createAreas(dtos));
    }
}
