package com.jawa.utsposclient.api.payload;

@SuppressWarnings("unused")
public record UpdateUserRequest(
    String username,
    String name,
    boolean resetPassword
) { }
