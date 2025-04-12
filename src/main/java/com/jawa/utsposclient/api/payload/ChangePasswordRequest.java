package com.jawa.utsposclient.api.payload;

public record ChangePasswordRequest(
    String oldPassword,
    String newPassword
) { }
