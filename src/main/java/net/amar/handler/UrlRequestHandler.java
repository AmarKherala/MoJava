package net.amar.handler;

import net.amar.Log;
import okhttp3.*;

public class UrlRequestHandler {

 private static final OkHttpClient client = new OkHttpClient();

 public static String fetchMojoLog(String url) {
  Request req = new Request.Builder().url(url).build();
  try (Response res = client.newCall(req).execute()) {
      if (res.body()==null) {
          Log.error("log body returned null, somehow.");
          return null;
      }
      return res.body().string();
    } catch (Exception e) {
      Log.error("Failed to frtch log from url ["+url+"]", e);
      return null;
    }
  } 
}
