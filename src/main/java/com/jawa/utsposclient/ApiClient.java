package com.jawa.utsposclient;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class ApiClient {
    private static ApiClient instance;
    private final OkHttpClient client;
    public static final String DOMAIN_URL = "http://localhost:8080";
    private static final String API_URL = DOMAIN_URL + "/api";
    public static final String SESSION_NAME = "USER_SESSION";
    private final Gson gson = new Gson();

    private final CookieManager cookieManager;

    private ApiClient() {
        this.cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        this.client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();

        // Load saved session from file
        SessionManager.loadLocalSession(cookieManager);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    private String getSessionCookie() {
        for (var cookie : cookieManager.getCookieStore().get(SessionManager.SESSION_URI)) {
            if (SESSION_NAME.equals(cookie.getName())) {
                return cookie.toString(); // Return the correct cookie format
            }
        }
        return "";
    }

    public Response get(String path) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + path)
                .addHeader("Cookie", getSessionCookie())
                .get()
                .build();
        return client.newCall(request).execute();
    }

    public Response post(String path, Object bodyObj) throws IOException {
        String jsonBody = gson.toJson(bodyObj);
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json")
        );
        Request request = new Request.Builder()
                .url(API_URL + path)
                .addHeader("Cookie", getSessionCookie())
                .post(body)
                .build();
        return client.newCall(request).execute();
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }
}