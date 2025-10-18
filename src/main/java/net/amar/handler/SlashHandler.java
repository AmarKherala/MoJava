package net.amar.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.amar.commands.slash.general.Verify;
import net.amar.commands.slash.staff.BanModDisplay;
import net.amar.commands.slash.staff.BanModRemove;
import net.amar.commands.slash.staff.BanModSupport;
import net.amar.commands.slash.SlashCommand;
import net.amar.commands.slash.general.BotInfo;
import net.amar.commands.slash.general.Help;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashHandler extends ListenerAdapter {
 
  private static final Map<String, SlashCommand> commands = new HashMap<>(); 

  public SlashHandler() {
    registerCommand(new BotInfo());
    registerCommand(new Help());
    registerCommand(new BanModSupport());
    registerCommand(new BanModDisplay());
    registerCommand(new BanModRemove());
    registerCommand(new Verify());
  }

  private void registerCommand(SlashCommand cmd) {
    commands.put(cmd.getName(), cmd);
  }

  @Override
  public void onSlashCommandInteraction( @NotNull SlashCommandInteractionEvent event) {
    String commandName = event.getName();
    SlashCommand cmd = commands.get(commandName); 
    if (cmd!=null) {
     cmd.executeSlash(event); 
    }
  }

  public static Map<String, SlashCommand> getCommandList(){
    return Collections.unmodifiableMap(commands);
  }
}