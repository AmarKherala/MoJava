package net.amar.mojo.commands.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import net.amar.mojo.commands.CmdInterface;
import net.amar.mojo.core.AmarLogger;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ModpingEdit implements CmdInterface{

    @Override
    public String getName() {
        return "modping-edit";
    }

    @Override
    public String getDescription() {
       return "add-remove a role to modping";
    }

    @SuppressWarnings("")
    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        String path ="src/main/resources/staff.json";
        try {
        Role role = event.getOption("role").getAsRole();
        String option = event.getOption("edit").getAsString();
        String json = new String(Files.readAllBytes(Paths.get(path)));

        JSONObject obj = new JSONObject(json);
        JSONArray arr =  obj.getJSONArray("modping_roles");

        boolean isFoundAndRemoved = false;
        if (option.equalsIgnoreCase("remove")){
            for (int index =0; index < arr.length() ; index++){             
                if (arr.get(index).equals(role.getId())){
                    arr.remove(index);
                    isFoundAndRemoved = true;
                    event.reply("Successfully removed role "+role.getName()+" from modping roles.").queue();
                    break;
                }
            }
            if (!isFoundAndRemoved) event.reply("Role "+role.getName()+" wasnt found in the modping roles..").queue();
        } else if (option.equalsIgnoreCase("add")){
            for (int index = 0; index < arr.length(); index++){
                if (arr.get(index).equals(role.getId())) event.reply("Role "+role.getName()+" already exists in modping roles!").queue();
                break;
            }
            arr.put(role.getId());
            event.reply("Successfully added role "+role.getName()+" to modping roles").queue();
        }
     
        try (FileWriter writer = new FileWriter(path)){
           writer.write(obj.toString());
           event.reply("Successfully added role: "+role.getName()+" to modping roles").queue();
           AmarLogger.info("User: "+event.getUser().getName()+" Added Role: "+role.getName()+" To Modping Roles");
        }
     }
     catch (IOException e){
      AmarLogger.error("Something went wrong while editing staff.json", e);
     }
    }
}
