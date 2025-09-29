package net.amar.mojo.commands.text.info;

import net.amar.mojo.commands.TextInterface;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements TextInterface {
    @Override
    public String Catagory() {
      return "info";
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String Description(){
      return "tells the bots ping to discord's gateway";
    }

    @Override
    public void executeMsg(MessageReceivedEvent event, String[] args) {
        long ping = event.getJDA().getGatewayPing();
        event.getMessage().replyFormat("Pong! the bots ping is %dms",ping).queue();
    }
}
