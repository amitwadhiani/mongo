package co.arctern.api.provider.controller;

import co.arctern.api.provider.service.LoginService;
import co.arctern.api.provider.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * apis for login/logout flow.
 */
@BasePathAwareController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private final TokenService tokenService;

    @Autowired
    public LoginController(LoginService loginService,
                           TokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    /**
     * to generate otp for login using phone number api.
     *
     * @param phone
     * @return
     * @throws Exception
     */
    @PostMapping("/generate-otp")
    public ResponseEntity<StringBuilder> generateOTP(@RequestParam("phone") String phone,
                                                     @RequestParam(value = "isAdmin", required = false, defaultValue = "false") Boolean isAdmin)
            throws Exception {
        return ResponseEntity.ok(loginService.generateOTP(phone, isAdmin));
    }

    /**
     * to verify the given otp with the otp and phone in otp api.
     *
     * @param phone
     * @param otp
     * @return
     * @throws Exception
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<OAuth2AccessToken> verifyOTP(@RequestParam("phone") String phone,
                                                       @RequestParam("otp") String otp)
            throws Exception {
        return ResponseEntity.ok(loginService.verifyOTP(phone, otp));

    }

    /**
     * to log out of the system api.
     *
     * @param userId
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/log-out")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<StringBuilder> logOut(@RequestParam(value = "userId", required = false) Long userId)
            throws Exception {
        if (userId == null) userId = tokenService.fetchUserId();
        return ResponseEntity.ok(loginService.logOut(userId));

    }

}
