package net.amar.commands.text.general;

import net.amar.commands.CommandCategories;
import net.amar.commands.text.TextCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements TextCommand{
    @Override
    public CommandCategories getCategory(){
        return CommandCategories.GENERAL;
    }

    @Override
    public String getName(){
        return "ping";
    }

    @Override
    public String getDescription(){
        return "tells the bots ping to discords gateway";
    }

    @Override
    public void executeMessage(MessageReceivedEvent event , String[] args){
        event.getMessage().replyFormat("pong! the bot's ping is %dms",event.getJDA().getGatewayPing()).queue();
    }
}
