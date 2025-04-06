package com.jawa.utsposclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static final String SESSION_FILE = "session.ser";
    public static final URI SESSION_URI = URI.create(ApiClient.DOMAIN_URL);
    private static final Gson gson = new Gson();

    public static void saveLocalSession(CookieManager cookieManager) {
        try(FileWriter writer = new FileWriter(SESSION_FILE)) {
            List<String> cookies = cookieManager.getCookieStore().get(SESSION_URI).stream()
                    .map(HttpCookie::toString)
                    .toList();
            gson.toJson(cookies, writer);
            System.out.println("[SessionManager] Session saved to " + SESSION_FILE);
        } catch (IOException e) {
            System.err.println("[SessionManager] Failed to save session: " + e.getMessage());
        }
    }

    public static void loadLocalSession(CookieManager cookieManager) {
        File file = new File(SESSION_FILE);
        if(!file.exists()) {
            System.out.println("[SessionManager] session file does not exist: " + SESSION_FILE);
            return;
        }

        try(FileReader reader = new FileReader(file)) {
            List<String> cookiesAsString = gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());
            for(String cookieAsString: cookiesAsString) {
                List<HttpCookie> parsedCookie = HttpCookie.parse(cookieAsString);
                if(!parsedCookie.isEmpty()) {
                    for(HttpCookie cookie: parsedCookie) {
                        cookieManager.getCookieStore().add(SESSION_URI, cookie);
                    }
                }
            }
            System.out.println("[SessionManager] loaded session");
        } catch (IOException e) {
            System.err.println("[SessionManager] Failed to load session: " + e.getMessage());
        }
    }

    public static void clearLocalSession(CookieManager cookieManager) {
        File file = new File(SESSION_FILE);
        if(file.exists() && file.delete()) {
            System.out.println("[SessionManager] session file deleted");
        } else {
            System.out.println("[SessionManager] No session file found");
        }

        // Hapus semua cookie dari cookie store
        CookieStore cookieStore = cookieManager.getCookieStore();
        List<URI> uris = new ArrayList<>(cookieStore.getURIs());
        for (URI uri : uris) {
            List<HttpCookie> cookies = new ArrayList<>(cookieStore.get(uri));
            for (HttpCookie cookie : cookies) {
                cookieStore.remove(uri, cookie);
            }
        }
        System.out.println("[SessionManager] cleared cookies from cookie store");

    }
}
