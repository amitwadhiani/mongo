package co.arctern.rider.api.controller;

import co.arctern.rider.api.security.jwt.TokenService;
import co.arctern.rider.api.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@BasePathAwareController
public class OTPController {

    @Autowired
    TokenService tokenService;

    @Autowired
    OTPService otpService;

    @GetMapping("/generate-token")
    public ResponseEntity<OAuth2AccessToken> generateToken(@RequestParam("phone") String phone,
                                                           @RequestParam("otp") String password) throws Exception {
        return ResponseEntity.ok(tokenService.retrieveToken(phone, password));
    }

    @GetMapping("/generate-otp")
    public ResponseEntity<String> generateOTP(@RequestParam("phone") String phone)
            throws Exception {
        return ResponseEntity.ok(otpService.generateOTP(phone));

    }

    @GetMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam("phone") String phone,
                                            @RequestParam("otp") String otp)
            throws Exception {
        return ResponseEntity.ok(otpService.verifyOTP(phone, otp));

    }

}
