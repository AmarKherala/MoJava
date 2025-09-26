package net.amar.mojo.commands.slash.mods;

import java.io.FileWriter;
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
public class BanMod implements SlashCommand {

    @Override
    public String getName() {
        return "ban-mod";
    }

    @Override
    public String getDescription() {
        return "add a mod to the ban list";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        String path = "src/main/resources/badMods.json";
        String toWrite = event.getOption("mod-id").getAsString();
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
            JSONObject mainObj = new JSONObject(jsonContent);
            JSONArray bannedMods = mainObj.getJSONArray("not_supported_mods");

            boolean isBanned = bannedMods.toString().contains(toWrite);

            if (isBanned) {
                event.replyFormat("The mod %s is already banned.", toWrite).queue();
                return;
            }

            bannedMods.put(toWrite);
            try (FileWriter write = new FileWriter(path)) {
                String newJsonObj = mainObj.toString();
                write.write(newJsonObj);
                event.replyFormat("Successfully banned support for mod: %s.", toWrite).queue();
                write.close();
            }
        } catch (IOException | JSONException e) {
            AmarLogger.error("Failed to add ModID [" + toWrite + "] to not_supported_mods array", e);
        }
    }
}
