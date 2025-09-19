package net.amar.mojo.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

public class LoadData {
    private static final Dotenv load = Dotenv.configure().directory("src/main/resources").load();

    public static JSONArray badMods() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/resources/badMods.json")));
            JSONObject obj = new JSONObject(json);
            JSONArray badMods = obj.getJSONArray("not_supported_mods");
            return badMods;
        } catch (IOException | JSONException e) {
            AmarLogger.error("Failed to fetch jsonarray (not_supported_mods)", e);
            return null;
        }
    }

    public static JSONArray staff(){
         try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/resources/staff.json")));
                JSONObject obj = new JSONObject(json);
                JSONArray staff = obj.getJSONArray("Staff_roles");
                return staff;
         } catch (JSONException | IOException e) {
            AmarLogger.error("[Failed to load staff role IDs]", e);
            return null;
         }
    }

    /*
     * All of the .env sections ^-^
     */

    public static String modRole(){
        return load.get("MOD_ROLE_ID");
    }
    public static String BotToken() {
        return load.get("BOT_TOKEN");
    }

    public static String prefix(){
        return load.get("BOT_PREFIX");
    }

    public static String forumChannelId() {
        return load.get("SUPPORT_FORUM_CHANNEL_ID");
    }

    public static String verdictChannel() {
        return load.get("VERDICT_CANNEL_ID");
    }

    public static String logChannel(){
        return load.get("LOG_CHANNEL_ID");
    }

    public static String supportBanned() {
        return load.get("SUP_BANNED_ROLE_ID");
    }

    public static String contRoleId(){
        return load.get("CONT_ROLE_ID");
    }
}
