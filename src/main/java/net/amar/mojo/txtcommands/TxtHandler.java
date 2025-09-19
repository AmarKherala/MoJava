package net.amar.mojo.txtcommands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.amar.mojo.core.LoadData;
import net.amar.mojo.txtcommands.commands.fun.modping;
import net.amar.mojo.txtcommands.commands.info.ping;
import net.amar.mojo.txtcommands.commands.modCommands.lockPost;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TxtHandler extends ListenerAdapter{
    static String prefix = LoadData.prefix();
    private final Map<String, TxtInterface> txtcmds = new HashMap<>();

    public TxtHandler(){
        register(new ping());
        register(new lockPost());
        register(new modping());
    }

    private void register(TxtInterface cmd) {
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

        TxtInterface cmd = txtcmds.get(TxtCmdName);

        if (cmd != null){
         cmd.executeMsg(event, args);
        }
    }
}
