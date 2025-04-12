package com.jawa.utsposclient.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jawa.utsposclient.enums.Role;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
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

    private static void setCookieExpiration(HttpCookie cookie) {
        // Extract the expiredAt timestamp from the cookie's value or session data
        String cookieValue = cookie.getValue();
        long expiredAtMillis = extractExpiredAtFromValue(cookieValue);

        // If expiredAtMillis is valid (non-zero), calculate the max age for the cookie
        if (expiredAtMillis > 0) {
            long currentTimeMillis = System.currentTimeMillis();
            long remainingTimeMillis = expiredAtMillis - currentTimeMillis;

            // Set max age based on the remaining time
            if (remainingTimeMillis > 0) {
                cookie.setMaxAge(remainingTimeMillis / 1000);  // Convert to seconds
            } else {
                // If expired, clear the cookie
                cookie.setMaxAge(0);
            }
            System.out.println("[SessionManager] Cookie expiration set to: " + remainingTimeMillis / 1000 + " seconds");
        }
    }

    private static long extractExpiredAtFromValue(String cookieValue) {
        // Assuming your cookie value is a JSON-like structure with expiredAt field
        try {
            String json = java.net.URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
            int expiredAtIndex = json.indexOf("\"expiredAt\":");
            if (expiredAtIndex > -1) {
                String expiredAtStr = json.substring(expiredAtIndex).replace("{", "").replace("}", "").split(":")[1];
                return Long.parseLong(expiredAtStr);
            }
        } catch (Exception e) {
            System.out.printf(e.toString());
        }
        return 0;  // Return 0 if expiredAt is not found
    }



    public static void loadLocalSession(CookieManager cookieManager) {
        File file = new File(SESSION_FILE);
        if(!file.exists()) {
            System.out.println("[SessionManager] session file does not exist: " + SESSION_FILE);
            return;
        }

        try(FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<String>>(){}.getType();
            List<String> cookiesAsString = gson.fromJson(reader, listType);
            for(String cookieAsString: cookiesAsString) {
                List<HttpCookie> parsedCookie = HttpCookie.parse(cookieAsString);
                if(!parsedCookie.isEmpty()) {
                    for(HttpCookie cookie: parsedCookie) {
                        setCookieExpiration(cookie);
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

    public static Role getSessionRole() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            System.out.println("[SessionManager] session file does not exist.");
            return null;
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> cookiesAsString = gson.fromJson(reader, listType);

            for (String cookie : cookiesAsString) {
                if (cookie.startsWith("USER_SESSION=")) {
                    String encodedJson = cookie.substring("USER_SESSION=".length());
                    String decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8);
                    JsonObject json = JsonParser.parseString(decodedJson).getAsJsonObject();
                    if (json.has("role")) {
                        return Role.valueOf(json.get("role").getAsString());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[SessionManager] Failed to read session file: " + e.getMessage());
        }

        return null;
    }

}
