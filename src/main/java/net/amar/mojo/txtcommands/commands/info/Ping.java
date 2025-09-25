package net.amar.mojo.txtcommands.commands.info;

import net.amar.mojo.txtcommands.TxtInterface;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements TxtInterface {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void executeMsg(MessageReceivedEvent event, String[] args) {
        long ping = event.getJDA().getGatewayPing();
        event.getMessage().replyFormat("Pong! the bots ping is %dms",ping).queue();
    }
}
