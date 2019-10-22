package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.response.projection.Areas;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaService extends MessageUtil {

    public static final String PIN_CODE_REGEXP = "^[0-9]*$";

    /**
     * assign areas to user.
     *
     * @param user
     * @param areaIds
     * @param roles
     */
    public void setAreasToUser(User user, List<Long> areaIds, List<Role> roles, Long clusterId);

    /**
     * fetch all areas.
     *
     * @param pageable
     * @return
     */
    public Page<Areas> fetchAreas(Pageable pageable);

    public List<String> search(String value);

    /**
     * fetch area by id .
     *
     * @param areaId
     * @return
     */
    public Area fetchById(Long areaId);

    public List<Area> fetchAreas(List<String> pinCodes);

    public void saveAll(List<Area> areas);

    public void save(Area area);

    public Boolean pincodeExists(String value);

}
