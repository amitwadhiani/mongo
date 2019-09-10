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

    public void setOfferingsToUser(User user, List<Long> offeringIds);

    public StringBuilder create(List<OfferingRequestDto> dtos);

    public Page<UserOffering> fetchUserOfferings(List<Long> offeringIds, Pageable pageable);

    public List<Offerings> fetchAll();

    public Offerings fetchById(Long id);
}
