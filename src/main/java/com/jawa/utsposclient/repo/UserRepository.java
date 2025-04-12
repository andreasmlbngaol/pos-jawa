package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.api.ApiResponse;
import com.jawa.utsposclient.api.payload.ChangePasswordRequest;
import com.jawa.utsposclient.response.OtpResponse;

import java.io.IOException;

public class UserRepository extends Repository {
    public static ApiResponse<Void> changePassword(long userId, String oldPassword, String newPassword) throws IOException {
        String path =  String.format("/users/%d/password/change", userId);
        var request = new ChangePasswordRequest(oldPassword, newPassword);
        var result = apiClient.patchParsed(path, request);
        System.out.println(result.getMessage());
        return result;
    }

    public static ApiResponse<OtpResponse> resetPassword(long userId) throws IOException {
        String path = String.format("/users/%d/password/reset", userId);
        return apiClient.patchParsed(path, OtpResponse.class);
    }
}
