package co.arctern.api.provider.service;

import co.arctern.api.provider.dto.response.HomePageResponse;

import java.time.ZonedDateTime;

public interface HomePageService {

    public HomePageResponse fetchHomePage(Long userId, ZonedDateTime start, ZonedDateTime end);


}
