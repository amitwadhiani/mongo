package co.arctern.rider.api.controller;

import co.arctern.rider.api.service.LoginService;
import co.arctern.rider.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@BasePathAwareController
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    /**
     * to generate otp for login using phone number.
     *
     * @param phone
     * @return
     * @throws Exception
     */
    @GetMapping("/generate-otp")
    public ResponseEntity<String> generateOTP(@RequestParam("phone") String phone)
            throws Exception {
        return ResponseEntity.ok(loginService.generateOTP(phone));
    }

    /**
     * to verify the given otp with the otp and phone in otp.
     *
     * @param phone
     * @param otp
     * @return
     * @throws Exception
     */
    @GetMapping("/verify-otp")
    public ResponseEntity<OAuth2AccessToken> verifyOTP(@RequestParam("phone") String phone,
                                                       @RequestParam("otp") String otp)
            throws Exception {
        return ResponseEntity.ok(loginService.verifyOTP(phone, otp));

    }

}
