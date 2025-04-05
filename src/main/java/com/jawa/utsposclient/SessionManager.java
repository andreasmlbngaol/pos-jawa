package com.jawa.utsposclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static final String SESSION_FILE = "session.ser";
    public static final URI SESSION_URI = URI.create(ApiClient.DOMAIN_URL);
    private static final Gson gson = new Gson();

    public static void saveSession(CookieManager cookieManager) {
        try(FileWriter writer = new FileWriter(SESSION_FILE)) {
            List<HttpCookie> cookies = cookieManager.getCookieStore().get(SESSION_URI);
            List<String> cookiesString = new ArrayList<>();
            for (HttpCookie cookie : cookies) {
                cookiesString.add(cookie.toString());
            }
            gson.toJson(cookiesString, writer);
            System.out.println("[SessionManager] saved session");
        } catch (IOException e) {
            System.err.println("[SessionManager] Failed to save session: " + e.getMessage());
        }
    }

    public static void loadSession(CookieManager cookieManager) {
        File file = new File(SESSION_FILE);
        if(!file.exists()) {
            System.out.println("[SessionManager] session file does not exist: " + SESSION_FILE);
            return;
        }

        try(FileReader reader = new FileReader(file)) {
            List<String> cookiesString = gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());

            for(String cookieStr: cookiesString) {
                HttpCookie cookie = HttpCookie.parse(cookieStr).get(0);
                cookieManager.getCookieStore().add(SESSION_URI, cookie);
            }

            System.out.println("[SessionManager] loaded session");
        } catch (IOException e) {
            System.err.println("[SessionManager] Failed to load session: " + e.getMessage());
        }
    }

    public static void clearSession() {
        File file = new File(SESSION_FILE);
        if(file.exists() && file.delete()) {
            System.out.println("[SessionManager] session file deleted");
        } else {
            System.out.println("[SessionManager] No session file found");
        }
    }
}
