package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface OfferingService extends MessageUtil {

    public void setOfferingsToUser(User user, List<Long> offeringIds);
}
