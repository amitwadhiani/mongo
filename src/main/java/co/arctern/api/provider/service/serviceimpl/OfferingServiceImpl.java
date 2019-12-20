package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.dao.OfferingDao;
import co.arctern.api.provider.dao.UserOfferingDao;
import co.arctern.api.provider.domain.Offering;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserOffering;
import co.arctern.api.provider.dto.request.OfferingRequestDto;
import co.arctern.api.provider.dto.response.projection.Offerings;
import co.arctern.api.provider.service.OfferingService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferingServiceImpl implements OfferingService {

    private final OfferingDao offeringDao;
    private final UserOfferingDao userOfferingDao;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public OfferingServiceImpl(OfferingDao offeringDao,
                               UserOfferingDao userOfferingDao,
                               ProjectionFactory projectionFactory) {
        this.offeringDao = offeringDao;
        this.userOfferingDao = userOfferingDao;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public void setOfferingsToUser(User user, List<Long> offeringIds) {
        List<UserOffering> userOfferings = new ArrayList<>();
        List<UserOffering> existingUserOfferings = user.getUserOfferings();
        if (!CollectionUtils.isEmpty(existingUserOfferings)) {
            userOfferingDao.deleteAll(existingUserOfferings);
        }
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
    @Transactional
    public StringBuilder create(List<OfferingRequestDto> dtos) {
        List<Offering> offerings = new ArrayList<>();
        dtos.stream().forEach(
                dto -> {
                    Offering offering = new Offering();
                    offering.setDescription(dto.getDescription());
                    offering.setType(dto.getType());
                    offering.setIsActive(true);
                    offerings.add(offering);
                });
        offeringDao.saveAll(offerings);
        return SUCCESS_MESSAGE;
    }

    @Override
    public Page<UserOffering> fetchUserOfferings(List<Long> offeringIds, Pageable pageable) {
        return userOfferingDao.findByOfferingIdInAndIsActiveTrue(offeringIds, pageable);
    }

    @Override
    public List<Offerings> fetchAll() {
        return Lists.newArrayList(offeringDao.findAll()).stream().map(offering -> projectionFactory.createProjection(Offerings.class, offering))
                .collect(Collectors.toList());
    }

    public Offerings fetchById(Long id) {
        return projectionFactory.createProjection(Offerings.class, offeringDao.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_OFFERING_ID_MESSAGE.toString());
        }));
    }

}
