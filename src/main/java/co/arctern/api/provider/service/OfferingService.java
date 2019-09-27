package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserOffering;
import co.arctern.api.provider.dto.request.OfferingRequestDto;
import co.arctern.api.provider.dto.response.projection.Offerings;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferingService extends MessageUtil {

    /**
     * map offering with user.
     * @param user
     * @param offeringIds
     */
    public void setOfferingsToUser(User user, List<Long> offeringIds);

    /**
     * create a new offering.
     * @param dtos
     * @return
     */
    public StringBuilder create(List<OfferingRequestDto> dtos);

    /**
     * fetch offerings for a user.
     * @param offeringIds
     * @param pageable
     * @return
     */
    public Page<UserOffering> fetchUserOfferings(List<Long> offeringIds, Pageable pageable);

    /**
     * fetch all offerings.
     * @return
     */
    public List<Offerings> fetchAll();

    /**
     * fetch by id.
     * @param id
     * @return
     */
    public Offerings fetchById(Long id);
}
