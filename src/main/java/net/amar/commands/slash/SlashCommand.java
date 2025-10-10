package net.amar.commands.slash;

import net.amar.commands.CommandCategories;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommand {

  CommandCategories getCategory();

  String getName();

  String getDescription();

  void executeSlash(SlashCommandInteractionEvent event);
}
