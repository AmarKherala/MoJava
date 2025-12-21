package net.amar.mojava.commands.slash.staff;

import net.amar.mojava.Load;
import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.slash.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONArray;

public class BanModDisplay implements SlashCommand {
    static StringBuilder modList;
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "mod-ban-list";
    }

    @Override
    public String getDescription() {
        return "displays the mods blacklist";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
     Load.reload();
     JSONArray array = Load.getUnsupportedMods();
        if (array==null) {
         event.reply("Couldn't load mod list array").queue();
         return;
     }

        modList = new StringBuilder();
        for (int i = 0; i < array.length(); i++) {
         modList.append(array.getString(i)).append("\n");
     }
        event.reply("```\n"+modList+"\n```").queue();
    }
}
