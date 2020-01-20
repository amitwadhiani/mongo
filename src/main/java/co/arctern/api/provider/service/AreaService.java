package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Address;
import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.dto.response.projection.ClustersWoArea;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService extends MessageUtil {

    /**
     * pinCode regex for validation.
     */
    public static final String PIN_CODE_REGEXP = "^[0-9]*$";

    /**
     * assign areas to user.
     *
     * @param user
     * @param roles
     */
    public void setAreasToUser(User user, List<Role> roles, List<Long> clusterIds);

    /**
     * fetch all areas.
     *
     * @param pageable
     * @return
     */
    public Page<Areas> fetchAreas(Pageable pageable);

    /**
     * area search.
     *
     * @param value
     * @return
     */
    public List<String> search(String value);

    /**
     * fetch area by id .
     *
     * @param areaId
     * @return
     */
    public Area fetchById(Long areaId);

    /**
     * fetch areas by pinCodes.
     *
     * @param pinCodes
     * @return
     */
    public List<Area> fetchAreas(List<String> pinCodes);

    /**
     * save areas.
     *
     * @param areas
     */
    public void saveAll(List<Area> areas);

    /**
     * save area (single entity) .
     *
     * @param area
     */
    public void save(Area area);

    /**
     * whether or not pinCode exists or not.
     *
     * @param value
     * @return
     */
    public Boolean pincodeExists(String value);

    /**
     * fetch areas by pinCode.
     *
     * @param pinCode
     * @return
     */
    public Areas fetchArea(String pinCode);

    /**
     * fetch area by pinCode.
     *
     * @param pinCode
     * @return
     */
    public Area fetchByPincode(String pinCode);

    /**
     * fetch cluster through address.
     *
     * @param address
     * @return
     */
    public ClustersWoArea getCluster(Address address);

}
