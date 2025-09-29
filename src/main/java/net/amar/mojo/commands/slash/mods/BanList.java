package net.amar.mojo.commands.slash.mods;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.amar.mojo.commands.SlashCommand;
import net.amar.mojo.core.AmarLogger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BanList implements SlashCommand {
    @Override
    public String Catagory() {
      return "mods";
    }
    @Override
    public String getName() {
        return "mod-list";
    }

    @Override
    public String getDescription() {
        return "banned mods list";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        String path = "src/main/resources/badMods.json";

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
            JSONObject mainObj = new JSONObject(jsonContent);
            JSONArray bannedMods = mainObj.getJSONArray("not_supported_mods");
           
             Pattern pattern = Pattern.compile("\"([^\"]+)\"");
             Matcher matcher = pattern.matcher(bannedMods.toString());
             StringBuilder mod = new StringBuilder();
             while(matcher.find()){
                mod.append(matcher.group(1)).append("\n");
             }
  
             if (mod.length() > 0) mod.setLength(mod.length() - 1);

            event.replyFormat("```js\n%s\n```", mod.toString()).queue();
        } catch (IOException | JSONException e) {
            AmarLogger.error("Failed to send banned-mods list", e);
            event.reply("Opps! I was unable to get banned mods list...").queue();
        }
    }
}
// yeah thats simple
