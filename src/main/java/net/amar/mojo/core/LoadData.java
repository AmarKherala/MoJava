package net.amar.mojo.core;

import io.github.cdimascio.dotenv.Dotenv;

public class LoadData {
    private static final Dotenv load = Dotenv.configure().directory("src/main/resources").load();

    public static String BotToken(){
        return load.get("BOT_TOKEN");
    }

    public static String forumChannelId(){
        return load.get("SUPPORT_FORUM_CHANNEL_ID");
    }

    public static String verdictChannel(){
        return load.get("VERDICT_CANNEL_ID");
    }

    public static String modRoleId(){
        return load.get("MOD_ROLE_ID");
    }

    public static String adminRoleId(){
        return load.get("ADMIN_ROLE_ID");
    }
}
