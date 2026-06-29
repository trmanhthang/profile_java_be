package com.example.profile.modules.user.controller;

import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.service.IAuthenticationService;
import com.example.profile.response.common.SuccessResponse;
import com.example.profile.shared.annotation.LogApi;
import com.example.profile.shared.constant.AuthenticationMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @LogApi
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        this.authenticationService.register(request);
        return SuccessResponse.setMessage(AuthenticationMessageConstant.REGISTER_MESSAGE);
    }
}
