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

public class MojangAPI {
    private static final String BASE_URL = "https://api.mojang.com";

    public static JsonObject minecraftProfile(String username) throws IOException {
        HttpURLConnection connection;

        URL url = new URL(BASE_URL + "/users/profiles/minecraft/" + username);
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
