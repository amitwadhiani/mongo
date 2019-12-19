package co.arctern.api.provider.service.serviceimpl;

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

    private final AddressDao addressDao;
    private final AreaService areaService;

    @Autowired
    public AddressServiceImpl(AddressDao addressDao,
                              AreaService areaService) {
        this.addressDao = addressDao;
        this.areaService = areaService;
    }

    @Override
    @SneakyThrows(Exception.class)
    public Address createOrFetchAddress(TaskAssignDto dto, Long addressId) {
        if (addressId != null) {
            Address address = addressDao.findById(addressId).orElseThrow(() ->
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ADDRESS_ID_MESSAGE.toString());
            });

            Long areaId = areaService.fetchArea(dto.getPinCode()).getId();
            if (areaId != null) {
                address.setArea(areaService.fetchById(areaId));
                address = addressDao.save(address);
            }
            return address;
        }
        return saveAddress(dto);
    }

    @Override
    public Address saveAddress(TaskAssignDto dto) {
        Address address = new Address();
        address.setName(dto.getAddressName());
        address.setHouseNumber(dto.getHouseNumber());
        address.setLine(dto.getLine());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        Boolean isSourceAddress = dto.getIsSourceAddress();
        address.setIsSourceAddress((isSourceAddress == null) ? false : isSourceAddress);
        address.setLocality(dto.getLocality());
        address.setLandmark(dto.getLandmark());
        address.setName(dto.getAddressName());
        address.setPinCode(dto.getPinCode());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        Long areaId = areaService.fetchArea(dto.getPinCode()).getId();
        if (areaId != null)
            address.setArea(areaService.fetchById(areaId));
        return addressDao.save(address);
    }

    @Override
    public Address fetchSourceAddress() {
        return addressDao.findByIsSourceAddressTrue().stream().findFirst().orElse(null);
    }
}
