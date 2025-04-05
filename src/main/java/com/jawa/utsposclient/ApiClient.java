package com.jawa.utsposclient;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.List;

public class ApiClient {
    private static ApiClient instance;
    private final OkHttpClient client;
    public static final String DOMAIN_URL = "http://localhost:8080";
    private static final String API_URL = DOMAIN_URL + "/api";
    private final Gson gson = new Gson();

    private final CookieManager cookieManager;

    private ApiClient() {
        this.cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
            Request request = chain.request();
            System.out.println("Request URL: " + request.url());
            System.out.println("Request Headers: " + request.headers());
            System.out.println("Request Cookies: " + cookieManager.getCookieStore().getCookies());

            Response response = chain.proceed(request);
            System.out.println("Response Code: " + response.code());
            return response;
        });
        this.client = builder
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();

//        this.client = new OkHttpClient.Builder()
//                .cookieJar(new JavaNetCookieJar(cookieManager))
//                .build();

        // Load saved session from file
        SessionManager.loadSession(cookieManager);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    private String getSessionCookie() {
        // Ambil cookies yang relevan
        List<HttpCookie> cookies = cookieManager.getCookieStore().get(SessionManager.SESSION_URI);
        for (HttpCookie cookie : cookies) {
            if ("USER_SESSION".equals(cookie.getName())) {
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
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(API_URL + path)
                .post(body)
                .build();
        return client.newCall(request).execute();
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }
}