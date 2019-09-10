package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.OfferingDao;
import co.arctern.api.provider.dao.UserOfferingDao;
import co.arctern.api.provider.domain.Offering;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserOffering;
import co.arctern.api.provider.dto.request.OfferingRequestDto;
import co.arctern.api.provider.service.OfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        List<UserOffering> existingUserOfferings = user.getUserOfferings();
        existingUserOfferings.stream().forEach(a -> a.setIsActive(false));
        userOfferingDao.saveAll(existingUserOfferings);
        offeringDao.findAllById(offeringIds).forEach(offering -> {
            UserOffering userOffering = new UserOffering();
            userOffering.setUser(user);
            userOffering.setIsActive(true);
            userOffering.setOffering(offering);
            userOfferings.add(userOffering);
        });
        userOfferingDao.saveAll(userOfferings);
    }

    @Override
    public StringBuilder create(List<OfferingRequestDto> dtos) {
        dtos.stream().forEach(
                dto -> {
                    Offering offering = new Offering();
                    offering.setDescription(dto.getDescription());
                    offering.setType(dto.getType());
                    offeringDao.save(offering);
                });
        return SUCCESS_MESSAGE;
    }

    @Override
    public Page<UserOffering> fetchUserOfferings(List<Long> offeringIds, Pageable pageable) {
        return userOfferingDao.findByOfferingIdInAndIsActiveTrue(offeringIds, pageable);
    }

}
