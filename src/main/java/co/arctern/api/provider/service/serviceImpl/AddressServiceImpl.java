package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AddressDao;
import co.arctern.api.provider.domain.Address;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.service.AddressService;
import io.undertow.util.BadRequestException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    @Override
    @SneakyThrows(BadRequestException.class)
    public Address createOrFetchAddress(TaskAssignDto dto) {
        Long addressId = dto.getAddressId();
        if (addressId != null) {
            return addressDao.findById(addressId).orElseThrow(() -> {
                        throw new BadRequestException("Invalid address id.");
                    }
            );
        }
        return saveAddress(dto);
    }

    @Override
    public Address saveAddress(TaskAssignDto dto) {
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setHouseNumber(dto.getHouseNumber());
        address.setLine(dto.getLine());
        address.setLocality(dto.getLocality());
        address.setPinCode(dto.getPinCode());
        address.setPatientId(dto.getPatientId());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        return addressDao.save(address);
    }
}