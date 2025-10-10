package net.amar.commands.slash.staff;

import net.amar.Load;
import net.amar.Log;
import net.amar.commands.CommandCategories;
import net.amar.commands.slash.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class BanModSupport implements SlashCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "mod-ban-support";
    }

    @Override
    public String getDescription() {
        return "adds a mod to the support blacklist";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        Load.reload();
       try {
           OptionMapping option = event.getOption("mod-id");
           if (option == null) return;

           if (Load.writeToBannedModsArray(option.getAsString())) {
               event.reply("successfully added mod ["+option.getAsString()+"] to mods blacklist").queue();
           } else {
               event.reply("["+option.getAsString()+"] already exists in the mods blacklist").queue();
           }
       } catch (Exception e) {
           Log.error("Error while writing to data.json file",e);
       }
    }
}
