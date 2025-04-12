package com.jawa.utsposclient.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class ApiClient {
    private static ApiClient instance;
    private final OkHttpClient client;
    private final Gson gson = new Gson();

    public static final String DOMAIN_URL = "http://localhost:8080";
    private static final String API_URL = DOMAIN_URL + "/api";
    public static final String SESSION_NAME = "USER_SESSION";

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

    private Request.Builder baseRequest(String path) {
        return new Request.Builder()
            .url(API_URL + path)
            .addHeader("Cookie", getSessionCookie());
    }

    private Response get(String path) throws IOException {
        return client.newCall(baseRequest(path).get().build()).execute();
    }

    public Response post(String path, Object bodyObj) throws IOException {
        String jsonBody = gson.toJson(bodyObj);
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json")
        );
        return client.newCall(baseRequest(path).post(body).build()).execute();
    }

    private Response patch(String path, Object bodyObj) throws IOException {
        String jsonBody = gson.toJson(bodyObj);
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json")
        );
        return client.newCall(baseRequest(path).patch(body).build()).execute();
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    private <T> ApiResponse<T> parseResponse(Response response, Class<T> dataClass) throws IOException {
        if(!response.isSuccessful() || response.body() == null)
            throw new IOException("Unexpected response: " + response);

        String body = response.body().string();
        Type type = TypeToken.getParameterized(ApiResponse.class, dataClass).getType();
        return gson.fromJson(body, type);
    }

    private ApiResponse<Object> parseErrorResponse(Response response) throws IOException {
        if(response.body() == null)
            throw new IOException("Empty body");

        String body = response.body().string();
        Type type = new TypeToken<ApiResponse<Object>>(){}.getType();
        return gson.fromJson(body, type);
    }

    private <T> ApiResponse<T> createErrorResponse(int code, String message) {
        return new ApiResponse<>(code, false, message, null);
    }

    public <T> ApiResponse<T> getParsed(String path, Class<T> dataClass) throws IOException {
        try (Response response = get(path)) {
            if (response.isSuccessful()) {
                return parseResponse(response, dataClass);
            } else {
                var error = parseErrorResponse(response);
                return createErrorResponse(error.code, error.message);
            }
        }
    }

    public ApiResponse<Void> getParsed(String path) throws IOException {
        return getParsed(path, Void.class);
    }

    public <T> ApiResponse<T> postParsed(String path, Object bodyObj, Class<T> dataClass) throws IOException {
        try(Response response = post(path, bodyObj)) {
            if (response.isSuccessful()) {
                return parseResponse(response, dataClass);
            } else {
                var error = parseErrorResponse(response);
                return createErrorResponse(error.getCode(), error.getMessage());
            }
        }
    }

    public ApiResponse<Void> postParsed(String path, Object bodyObj) throws IOException {
        return postParsed(path, bodyObj, Void.class);
    }

    public <T> ApiResponse<T> postParsed(String path, Class<T> dataClass) throws IOException {
        return postParsed(path, new Object(), dataClass);
    }

    public ApiResponse<Void> postParsed(String path) throws IOException {
        return postParsed(path, Void.class);
    }

    public <T> ApiResponse<T> patchParsed(String path, Object bodyObj, Class<T> dataClass) throws IOException {
        try (Response response = patch(path, bodyObj)) {
            if (response.isSuccessful()) {
                return parseResponse(response, dataClass);
            } else {
                var error = parseErrorResponse(response);
                return createErrorResponse(error.code, error.message);
            }
        }
    }

    public ApiResponse<Void> patchParsed(String path, Object bodyObj) throws IOException {
        return patchParsed(path, bodyObj, Void.class);
    }

    public <T> ApiResponse<T> patchParsed(String path, Class<T> dataClass) throws IOException {
        return patchParsed(path, new Object(), dataClass);
    }

    public ApiResponse<Void> patchParsed(String path) throws IOException {
        return patchParsed(path, Void.class);
    }
}