package net.amar.mojo.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.amar.mojo.commands.TextInterface;
import net.amar.mojo.commands.text.fun.Mopding;
import net.amar.mojo.commands.text.info.Help;
import net.amar.mojo.commands.text.info.Ping;
import net.amar.mojo.commands.text.moderators.LockPost;
import net.amar.mojo.core.LoadData;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TextCmdHandler extends ListenerAdapter{
    static String prefix = LoadData.prefix();
    private static final Map<String, TextInterface> txtcmds = new HashMap<>();

    public TextCmdHandler(){
        // info
        register((TextInterface) new Ping());
        register((TextInterface) new Help());

        //fun
        register((TextInterface) new Mopding());
        
        // moderation
        register(new LockPost());
    }

    private void register(TextInterface cmd) {
    txtcmds.put(cmd.getName().toLowerCase(), cmd);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
      if (event.getAuthor().isBot()) return;
      if (prefix == null || prefix.isBlank()){
            prefix = "?";
        }
        String rawMsg = event.getMessage().getContentRaw();
     
        if (!rawMsg.toLowerCase().startsWith(prefix) || rawMsg.length() <= prefix.length()) return;

        String[] parts = rawMsg.substring(prefix.length()).split("\\s+");
        String TxtCmdName = parts[0].toLowerCase();
        
        String[] args = Arrays.copyOfRange(parts,1,parts.length);

        TextInterface cmd = txtcmds.get(TxtCmdName);

        if (cmd != null){
         cmd.executeMsg(event, args);
        }
    }

    public static String infoCommands() {
       StringBuilder info = new StringBuilder();
         for (TextInterface cmd: txtcmds.values()){
           if (cmd.Catagory().equals("info")){
             info.append("**__"+cmd.getName()+"__** :").append(cmd.Description()).append("\n");           
           }
         }
       return info.toString();
    }

    public static String funCommands() {
      StringBuilder fun = new StringBuilder();
      for (TextInterface cmd: txtcmds.values()){
        if (cmd.Catagory().equals("fun")){
          fun.append("**__"+cmd.getName()+"__** :").append(cmd.Description()).append("\n");
        }
      }
      return fun.toString();
    }

    public static  String modCommands() {
      StringBuilder mod = new StringBuilder();
      for (TextInterface cmd: txtcmds.values()){
        if (cmd.Catagory().equals("staff")){
          mod.append("**__"+cmd.getName()+"__** :").append(cmd.Description()).append("\n");
        }
      }
      return mod.toString();
    }
}
