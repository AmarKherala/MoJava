package net.amar.mojava.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.amar.mojava.Load;
import net.amar.mojava.Main;
import net.amar.mojava.commands.text.TextCommand;
import net.amar.mojava.commands.text.general.Help;
import net.amar.mojava.commands.text.general.Ping;
import net.amar.mojava.commands.text.staff.LockThread;
import net.amar.mojava.commands.text.staff.SetOnlineStatus;
import net.amar.mojava.commands.text.staff.SetStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TextHandler extends ListenerAdapter {

  private static final Map<String, TextCommand> commands = new HashMap<>();
  static String prefix = Load.getBotPrefix();

  public TextHandler() {
    registerCommands(new Help());
    registerCommands(new LockThread());
    registerCommands(new Ping());
    registerCommands(new SetStatus());
    registerCommands(new SetOnlineStatus());
  }

  private void registerCommands(TextCommand cmd) {
    commands.put(cmd.getName().toLowerCase(), cmd);
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
   /* to stop that annoying exception
   "java.lang.IllegalStateException: This message event did not happen in a guild"
    */
    if (!event.isFromGuild()) return;


    if ( event.getGuild().equals(Main.getJDA().getGuildById(Load.getGuildId()))) return;

    String rawMessage = event.getMessage().getContentRaw().toLowerCase();

    if (rawMessage.startsWith(prefix) && rawMessage.length() > prefix.length()) {
      String[] parts = rawMessage.substring(prefix.length()).split("\\s+");
      String txtCmdName = parts[0].toLowerCase();
      String[] args = Arrays.copyOfRange(parts,1, parts.length);

      TextCommand cmd = commands.get(txtCmdName);

      if (cmd!=null) {
        cmd.executeMessage(event, args);
      }
    }
  }

  public static Map<String, TextCommand> getTextCmdMap() {
    return Collections.unmodifiableMap(commands);
  }
}
