package net.amar.mojo.commands.slash.mods;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.amar.mojo.commands.SlashCommand;
import net.amar.mojo.core.AmarLogger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SuppressWarnings("null")
public class UnBanMod implements SlashCommand {

    @Override
    public String getName() {
        return "unban_mod";
    }

    @Override
    public String getDescription() {
        return "remove a mod from ban list";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        String path = "src/main/resources/badMods.json";
        String toRemove = event.getOption("mod-id").getAsString();
        try {
            String jsonContent = Files.readString(Paths.get(path));
            JSONObject mainObj = new JSONObject(jsonContent);
            JSONArray bannedMods = mainObj.getJSONArray("not_supported_mods");

            boolean removed = false;

            for (int i = 0; i < bannedMods.length(); i++) {
                if (bannedMods.getString(i).equals(toRemove)) {
                    bannedMods.remove(i);
                    removed = true;
                    break;
                }
            }

            if (removed) {
                Files.writeString(Paths.get(path), mainObj.toString());
                event.replyFormat("Successfully deleted Mod [%s] from ban list", toRemove).queue();
            } else {
                event.replyFormat("Couldn't find Mod [%s] on the ban list", toRemove).queue();
            }

        } catch (IOException | JSONException e) {
            AmarLogger.error("Error deleting mod from ban list", e);
        }
    }
}
