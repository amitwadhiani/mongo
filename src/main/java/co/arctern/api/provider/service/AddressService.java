package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Address;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.util.MessageUtil;

public interface AddressService extends MessageUtil {

    /**
     * create or fetch address.
     *
     * @param dto
     * @param addressId
     * @return
     */
    Address createOrFetchAddress(TaskAssignDto dto, Long addressId);

    /**
     * save address through dto.
     *
     * @param dto
     * @return
     */
    Address saveAddress(TaskAssignDto dto);

}