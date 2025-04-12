package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.api.ApiResponse;
import com.jawa.utsposclient.api.payload.LoginRequest;
import com.jawa.utsposclient.dto.User;

import java.io.IOException;

public class AuthRepository extends Repository {
    public static ApiResponse<User> login(String username, String password) throws IOException {
        return apiClient.postParsed(
            "/login",
            new LoginRequest(username, password),
            User.class
        );
    }

    public static ApiResponse<Void> logout() throws IOException {
        return apiClient.postParsed("/logout");
    }
}
