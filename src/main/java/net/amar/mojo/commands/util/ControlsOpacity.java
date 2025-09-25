package net.amar.mojo.commands.util;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

import net.amar.mojo.commands.CmdInterface;
import net.amar.mojo.core.AmarLogger;
import net.amar.mojo.handler.RequestsHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SuppressWarnings("")
public class ControlsOpacity implements CmdInterface {

    @Override
    public String getName() {
        return "set-opacity";
    }

    @Override
    public String getDescription() {
        return "change the opacity of ur mojo buttons";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        try {
            Double opacity = event.getOption("value").getAsDouble();
            var jsonOption = event.getOption("json");
            if (jsonOption == null || jsonOption.getAsAttachment() == null) {
                event.reply("You must provide a JSON file.").queue();
                return;
            }

            String jsonUrl = jsonOption.getAsAttachment().getUrl();
            String fileName = jsonOption.getAsAttachment().getFileName(); // capture filename

            String jsom = RequestsHandler.fetchLog(jsonUrl);
            JSONObject obj = new JSONObject(jsom);

            if (opacity > 100 || opacity < 0) {
                event.reply("opacity cant be negatuve/bigger than 100").queue();
                return;
            }

            Double newValue = opacity / 100;
            replaceOpacity(obj, newValue);

            byte[] jsonBytes = obj.toString(4).getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream stream = new ByteArrayInputStream(jsonBytes);

            event.reply("thinking...").queue(hook -> {
                hook.sendMessage("Success!")
                        .addFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(stream, fileName))
                        .queue(Msg ->{
                    hook.deleteOriginal().queue();
                        });
            });

        } catch (JSONException e) {
            AmarLogger.error("Something went wrong while parsing controls json...", e);
        }
    }

   private void replaceOpacity(Object obj, double value) {
        switch (obj) {
            case JSONObject json -> {
                for (String key : json.keySet()) {
                    Object val = json.get(key);
                    if (key.equalsIgnoreCase("opacity")) {
                        json.put(key, value);
                    } else if (val instanceof JSONObject || val instanceof org.json.JSONArray) {
                        replaceOpacity(val, value);
                    }
                }
            }       case org.json.JSONArray arr -> {
                for (int i = 0; i < arr.length(); i++) {
                    replaceOpacity(arr.get(i), value);
                }
            }       default -> {
            }
        }
}

}
