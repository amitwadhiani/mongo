package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.projection.PaymentsForUser;

import java.util.List;

public interface GenericService {

    /**
     * fetch amount owed by user.
     *
     * @param userId
     * @return
     */
    public Double fetchUserOwedAmount(Long userId);

    List<PaymentsForUser> fetchPaymentInfo(Long userId);

}
