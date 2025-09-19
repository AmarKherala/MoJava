package net.amar.mojo.txtcommands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TxtInterface{
 String getName();
 void executeMsg(MessageReceivedEvent event, String[] args);
}