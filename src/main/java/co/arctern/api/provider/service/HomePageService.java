package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.HomePageResponse;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public interface HomePageService {

    public HomePageResponse fetchHomePage(Long userId, Timestamp start, Timestamp end);


}
