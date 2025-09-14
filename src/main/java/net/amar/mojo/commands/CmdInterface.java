package net.amar.mojo.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CmdInterface {
   
    String getName();

    String getDescription();

    void executeSlash(SlashCommandInteractionEvent event);
}
