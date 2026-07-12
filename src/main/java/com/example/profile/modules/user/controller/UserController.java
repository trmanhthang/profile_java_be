package com.example.profile.modules.user.controller;

import com.example.profile.modules.user.request.RegisterRequest;
import com.example.profile.shared.annotation.LogApi;
import com.example.profile.shared.common.ApiResponse;
import com.example.profile.shared.constant.AuthenticationMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @LogApi
    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request) {
        return ApiResponse.custom(null, AuthenticationMessageConstant.REGISTER_MESSAGE, HttpStatus.CREATED);
    }
}
