package net.amar.mojo.events;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import net.amar.mojo.commands.CmdInterface;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
    
private final Map <String, CmdInterface> commands = new HashMap<>();

public Commands(){
  
}
@Override
public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
    String name = event.getName();
    CmdInterface cmd = commands.get(name);
    cmd.executeSlash(event);
  }
}
