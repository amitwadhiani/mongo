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

    /**
     * fetch paginated payment-info based on userId.
     *
     * @param userId
     * @param pageable
     * @return
     */
    PaginatedResponse fetchPaymentInfo(Long userId, Pageable pageable);

    /**
     * fetch paginated payment-info based on userId (from repo ) .
     *
     * @param userId
     * @param pageable
     * @return
     */
    public PaginatedResponse getPaymentsForUser(Long userId, Pageable pageable);

    /**
     * fetch payments received by user.
     *
     * @param userId
     * @return
     */
    public List<Payments> getReceivedPaymentsForUser(Long userId);

    /**
     * fetch user by id.
     *
     * @param userId
     * @return
     */
    public User fetchUser(Long userId);

}
