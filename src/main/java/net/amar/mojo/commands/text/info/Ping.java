package net.amar.mojo.commands.text.info;

import net.amar.mojo.commands.TextInterface;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements TextInterface {

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
