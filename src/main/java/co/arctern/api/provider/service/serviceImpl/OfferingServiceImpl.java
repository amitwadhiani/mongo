package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.OfferingDao;
import co.arctern.api.provider.dao.UserOfferingDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserOffering;
import co.arctern.api.provider.service.OfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferingServiceImpl implements OfferingService {

    @Autowired
    OfferingDao offeringDao;

    @Autowired
    UserOfferingDao userOfferingDao;

    @Override
    public void setOfferingsToUser(User user, List<Long> offeringIds) {
        List<UserOffering> userOfferings = new ArrayList<>();
        offeringDao.findAllById(offeringIds).forEach(offering -> {
            UserOffering userOffering = new UserOffering();
            userOffering.setUser(user);
            userOffering.setIsActive(true);
            userOffering.setOffering(offering);
            userOfferings.add(userOffering);
        });
        userOfferingDao.saveAll(userOfferings);
    }
}
