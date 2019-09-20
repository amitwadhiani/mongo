package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AddressDao;
import co.arctern.api.provider.domain.Address;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.AddressService;
import co.arctern.api.provider.service.AreaService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    @Autowired
    AreaService areaService;

    @Override
    @SneakyThrows(Exception.class)
    public Address createOrFetchAddress(TaskAssignDto dto, Long addressId) {
        if (addressId != null) return addressDao.findById(addressId).orElseThrow(() ->
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role id.");
        });
        return saveAddress(dto);
    }

    @Override
    public Address saveAddress(TaskAssignDto dto) {
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setName(dto.getAddressName());
        address.setHouseNumber(dto.getHouseNumber());
        address.setLine(dto.getLine());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setLocality(dto.getLocality());
        address.setLandmark(dto.getLandmark());
        address.setPinCode(dto.getPinCode());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
//        address.setArea(areaService.fetchById(dto.getAreaId()));
        return addressDao.save(address);
    }
}
