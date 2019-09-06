package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;
import java.util.Map;

/**
 * To retrieve token after validation using phone and otp
 */
public interface TokenService extends MessageUtil {

    /**
     * to retrieve token via username and password(otp, in our case).
     *
     * @param phone
     * @param otp
     * @return
     */
    public OAuth2AccessToken retrieveToken(String phone, String otp);

    /**
     * fetch user id from token.
     *
     * @return
     */
    public Long fetchUserId();

    /**
     * fetch areas from token.
     *
     * @return
     */
    public List<Area> fetchAreasFromToken();

    /**
     * to fetch area ids from token.
     *
     * @return
     */
    public List<Long> fetchAreaIds();

    /**
     * to fetch authorities from token.
     *
     * @return
     */
    public List<String> fetchAuthorities();

    /**
     * to extract access token.
     *
     * @return
     */
    public Map<String, Object> extractAccessToken();

}
