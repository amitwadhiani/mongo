package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.dto.response.projection.PaymentsForUser;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface GenericService extends MessageUtil {

    /**
     * fetch amount owed by user.
     *
     * @param userId
     * @return
     */
    public Double fetchUserOwedAmount(Long userId);

    List<PaymentsForUser> fetchPaymentInfo(Long userId);

    public List<Payments> getPaymentsForUser(Long userId);

    public List<Payments> getReceivedPaymentsForUser(Long userId);

    public User fetchUser(Long userId);

}
