package net.amar.commands.slash.staff;

import net.amar.Load;
import net.amar.commands.CommandCategories;
import net.amar.commands.slash.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class BanModRemove implements SlashCommand{
    @Override
    public CommandCategories getCategory(){
        return CommandCategories.STAFF;
    }

    @Override
    public String getName(){
        return "mod-ban-remove";
    }

    @Override
    public String getDescription(){
        return "remove a mod from the mods blacklist";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event){
        OptionMapping option = event.getOption("mod-id");
        String modId = option != null ? option.getAsString() : null;

        Load.reload();
        if (Load.removeFromBannedModsArray(modId))
            event.reply("successfully removed ["+modId+"] from the blacklist").queue();
        else event.reply("couldn't remove mod ["+modId+"] from the blacklist").queue();
    }
}
