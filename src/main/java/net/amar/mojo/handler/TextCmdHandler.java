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
    private final Map<String, TextInterface> txtcmds = new HashMap<>();

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
        if (prefix == null || prefix.isBlank()){
            prefix = "?";
        }
        String rawMsg = event.getMessage().getContentRaw();

        if (event.getAuthor().isBot() || !rawMsg.startsWith(prefix)) return;

        String[] parts = rawMsg.substring(prefix.length()).split("\\s+");
        String TxtCmdName = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts,1,parts.length);

        TextInterface cmd = txtcmds.get(TxtCmdName);

        if (cmd != null){
         cmd.executeMsg(event, args);
        }
    }
}
