package com.example.profile.modules.user.controller;

import com.example.profile.modules.user.request.LoginRequest;
import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.response.AuthenticationResponse;
import com.example.profile.modules.user.service.IAuthenticationService;
import com.example.profile.shared.common.ApiResponse;
import com.example.profile.shared.annotation.LogApi;
import com.example.profile.shared.constant.AuthenticationMessageConstant;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request) {
        this.authenticationService.register(request);
        return ApiResponse.custom(null, AuthenticationMessageConstant.REGISTER_MESSAGE, HttpStatus.CREATED);
    }

    @LogApi
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        return ApiResponse.custom(this.authenticationService.login(request, response), AuthenticationMessageConstant.LOGIN_SUCCESS, HttpStatus.OK);
    }

    
}
