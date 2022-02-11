package gg.codie.minecraft.api;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import net.glasslauncher.wrapper.WrapperUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SessionServer {
    private static final String BASE_URL = "https://sessionserver.mojang.com";

    public static boolean joinGame(String accessToken, String selectedProfile, String serverId) throws IOException {
        HttpURLConnection connection;

        JsonObject jsonObject = new JsonObject();
        jsonObject.put("accessToken", new JsonPrimitive(accessToken));
        jsonObject.put("selectedProfile", new JsonPrimitive(selectedProfile));
        jsonObject.put("serverId", new JsonPrimitive(serverId));

        String json = jsonObject.toString();

        URL url = new URL(BASE_URL + "/session/minecraft/join");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        connection.getOutputStream().flush();
        connection.getOutputStream().close();

        connection.connect();

        return connection.getResponseCode() == 204;
    }

    public static boolean hasJoined(String username, String serverId, String ip) throws IOException {
        HttpURLConnection connection;

        URL url = new URL(BASE_URL + "/session/minecraft/hasJoined?username=" + username + "&serverId=" + serverId + (ip != null ? "&ip=" + ip : ""));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(false);

        connection.connect();

        return connection.getResponseCode() == 200;
    }

    public static JsonObject minecraftProfile(String uuid) throws IOException {
        HttpURLConnection connection;

        URL url = new URL(BASE_URL + "/session/minecraft/profile/" + uuid);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(false);

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        try {
            return (JsonObject) WrapperUtils.JANKSON.toJson(response.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            JsonObject errorObject = new JsonObject();
            errorObject.put("error", new JsonPrimitive(response.toString()));
            return errorObject;
        }
    }
}
