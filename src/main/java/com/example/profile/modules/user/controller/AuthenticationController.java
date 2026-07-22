package com.example.profile.modules.user.controller;

import com.example.profile.modules.user.request.LoginRequest;
import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.modules.user.response.AccessTokenResponse;
import com.example.profile.modules.user.response.AuthenticationResponse;
import com.example.profile.modules.user.service.IAuthenticationService;
import com.example.profile.shared.common.ApiResponse;
import com.example.profile.shared.annotation.LogApi;
import com.example.profile.shared.constant.AuthenticationMessageConstant;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.custom(this.authenticationService.login(request, response),
                                  AuthenticationMessageConstant.LOGIN_SUCCESS,
                                  HttpStatus.OK);
    }

    @LogApi
    @RequestMapping(method = RequestMethod.GET, value = "/refresh")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refresh(@CookieValue(value = "refresh_token") String refreshToken) {
        AccessTokenResponse response = this.authenticationService.refresh(refreshToken);
        return ApiResponse.custom(response, AuthenticationMessageConstant.REGISTER_MESSAGE, HttpStatus.OK);
    }
}
