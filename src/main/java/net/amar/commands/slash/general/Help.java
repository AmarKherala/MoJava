package net.amar.commands.slash.general;

import java.util.Map;
import java.util.stream.Collectors;

import net.amar.Log;
import net.amar.commands.CommandCategories;
import net.amar.commands.slash.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.amar.handler.SlashHandler;
public class Help implements SlashCommand {

  static final StringBuilder prettyCmdList = new StringBuilder();
  
	@Override
	public CommandCategories getCategory() {
		return CommandCategories.GENERAL;
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "shows a list of slash commands";
	}

	@Override
	public void executeSlash(SlashCommandInteractionEvent event) {

    
    if (!prettyCmdList.toString().isEmpty()) {
      event.reply("**Slash Commands**"+prettyCmdList+"\n-# for text commands do m!help").queue();
		Log.info("using the already cached slash commands list");
      return;
    }
	Map<String, SlashCommand> commands = SlashHandler.getCommandList();
    for (CommandCategories cat : CommandCategories.values()) {
        String list = commands.values().stream()
            .filter(cmd -> cmd.getCategory() == cat)
            .map(cmd -> "`/" + cmd.getName() + "` — " + cmd.getDescription())
            .collect(Collectors.joining("\n"));

        if (!list.isEmpty()) {
            prettyCmdList.append("**").append(cat.name()).append(" Commands:**\n");
            prettyCmdList.append(list).append("\n\n");
        }
    }

    event.reply("**Slash Commands**\n"+prettyCmdList+"\n-# for text commands do m!help").queue();
	}
}
