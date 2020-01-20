package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericService extends MessageUtil {

    /**
     * fetch amount owed by user.
     *
     * @param userId
     * @return
     */
    public Double fetchUserOwedAmount(Long userId);

    PaginatedResponse fetchPaymentInfo(Long userId, Pageable pageable);

    public PaginatedResponse getPaymentsForUser(Long userId, Pageable pageable);

    public List<Payments> getReceivedPaymentsForUser(Long userId);

    public User fetchUser(Long userId);

}
