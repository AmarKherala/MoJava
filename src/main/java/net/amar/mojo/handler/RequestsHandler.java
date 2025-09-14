/*
 * This will be my way to parse them logs
 * -amar
 */

package net.amar.mojo.handler;

import net.amar.mojo.core.AmarLogger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestsHandler {
    
    private static final OkHttpClient client = new OkHttpClient();
    @SuppressWarnings("null")
    public static String fetchLog(String log){
        Request req = new Request.Builder().url(log).build();

        try(Response res = client.newCall(req).execute()){
      return res.body().string();
        } catch (Exception e){
            AmarLogger.error("OkHttp request failed..",e);
            return null;
        }
    }
}
