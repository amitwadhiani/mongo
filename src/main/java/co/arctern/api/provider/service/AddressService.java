package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Address;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.util.MessageUtil;

public interface AddressService extends MessageUtil {

    Address createOrFetchAddress(TaskAssignDto dto);

    Address saveAddress(TaskAssignDto dto);

}