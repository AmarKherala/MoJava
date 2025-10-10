package net.amar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import org.json.JSONArray;
import org.json.JSONObject;

public class Load {

    private static final String path="src/main/resources/data.json";
    private static JSONObject jsonObject;
    
    static {
       getJsonObject();
    }

    private static void getJsonObject() {
        if(jsonObject!=null) return;
        try {
            File file=new File(path);
             
              if (!file.exists()) {
                 Log.error("Couldn't load or find file ["+path+"]");
                 return;
              }

            String lines=Files.readString(file.toPath());
            jsonObject =new JSONObject(lines);
        } catch ( IOException e ) {
            Log.error("failed to load json from "+path, e);
        }
    }

    public static void reload() {
        try {
            String lines = Files.readString(Paths.get(path));
            jsonObject = new JSONObject(lines);
        } catch (IOException e) {
            Log.error("Failed to reload file ["+path+"]");
        }
    }

    // getters
    public static String getBotToken() {
        return jsonObject!=null ? jsonObject.getString("token") : null;
    }
    
    public static String getBotPrefix() {
        return jsonObject!=null ? jsonObject.getString("prefix") : null;
    }
  
    public static String getGuildId() {
        return jsonObject!=null ? jsonObject.getString("mojo_id") : null;
    }

    public static String getLogChannelId() {
        return jsonObject!=null ? jsonObject.getString("log_channel") : null;
    }

    public static String getVerdictChannelId() {
        return jsonObject!=null ? jsonObject.getString("verdict_id") : null;
    }
    public static String getSupportForumId() {
        return jsonObject!=null ? jsonObject.getString("support_forum") : null;
    }

    public static JSONArray getStaffRoles() {
        return jsonObject.getJSONArray("staff_roles");
    }

    public static String getPretesterRoleId() {
        return jsonObject!=null ? jsonObject.getString("pre_tester") : null;
    }
    /* Can't lazy load this one, it may get updated on runtime
     *via #BanModSupport.java or #BadModRemove.java */
    public static JSONArray getUnsupportedMods() {
        try {
            String json=Files.readString(Paths.get(path));
            JSONObject obj=new JSONObject(json);
            return obj.getJSONArray("no_support_mods");
        } catch ( IOException e ) {
            return null;
        }
    }

    // not getters, I don't know what to call those they're more like methods
    public static boolean writeToBannedModsArray(String modId) {
        try ( BufferedWriter writer=new BufferedWriter(new FileWriter(path)) ) {
            JSONObject obj=jsonObject;
            JSONArray arr=obj!=null ? obj.getJSONArray("no_support_mods") : null;
            if(arr==null) {
                Log.error("the blacklist listed  mods array is empty");
                return false;
            }
            for(int i=0 ; i<arr.length() ; i++) {
                if(arr.getString(i).equals(modId)) {
                    return false;
                }
            }
            arr.put(modId);
            writer.write(obj.toString(4));
            return true;
        } catch ( Exception e ) {
            Log.error("Error while writing to file ["+path+"]." , e);
            return false;
        }
    }

    public static boolean removeFromBannedModsArray(String modId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            JSONObject obj = jsonObject;
            JSONArray arr = obj != null ? obj.getJSONArray("no_support_mods") : null;
            boolean hasChanged = false;
            if (arr==null) return false;
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getString(i).equals(modId)) {
                    arr.remove(i);
                    hasChanged = true;
                    break;
                }
            }
            if (hasChanged) {
                writer.write(obj.toString(4));
                return true;
            }
            return false;
        } catch (IOException e) {
            Log.error("error while removing from file ["+path+"]");
            return false;
        }
    }
}

