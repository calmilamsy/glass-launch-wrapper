package gg.codie.mineonline.utils;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonObject;
import gg.codie.minecraft.api.MojangAPI;
import gg.codie.minecraft.api.SessionServer;
import net.glasslauncher.wrapper.WrapperUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Base64;

public class SkinUtils {
    public static JsonObject getUserSkin(String username) {
        try {
            JsonObject profile = MojangAPI.minecraftProfile(username);
            if (!profile.containsKey("id"))
                throw new FileNotFoundException("User not found: " + username);
            profile = SessionServer.minecraftProfile(profile.get(String.class, "id"));
            if (!profile.containsKey("properties"))
                throw new FileNotFoundException("Skin not found: " + username);
            profile = (JsonObject) WrapperUtils.JANKSON.toJson(new String(Base64.getDecoder().decode(profile.get(JsonArray.class, "properties").get(JsonObject.class, 0).get(String.class, "value")), StandardCharsets.UTF_8));
            return profile.get(JsonObject.class, "textures").get(JsonObject.class, "SKIN");

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static URL findEventCloakURLForUsername(String username) {
        try {
            LocalDateTime today = java.time.LocalDateTime.now();
            if ((today.getDayOfMonth() == 24 || today.getDayOfMonth() == 25) && today.getMonth() == Month.DECEMBER)
                return SkinUtils.class.getResource("/textures/mineonline/capes/xmas.png");

            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public static URL findCloakURLForUsername(String username) {
        try {
            JsonObject profile = MojangAPI.minecraftProfile(username);
            if (!profile.containsKey("id"))
                throw new FileNotFoundException("User not found: " + username);

            profile = SessionServer.minecraftProfile(profile.get(String.class, "id"));
            if (!profile.containsKey("properties"))
                throw new FileNotFoundException("Cloak not found: " + username);
            profile = (JsonObject) WrapperUtils.JANKSON.toJson(new String(Base64.getDecoder().decode(profile.get(JsonArray.class, "properties").get(JsonObject.class, 0).get(String.class, "value")), StandardCharsets.UTF_8));
            return new URL(profile.get(JsonObject.class, "textures").get(JsonObject.class, "CAPE").get(String.class, "url"));
        } catch (Exception ex) {
            return null;
        }
    }
}
