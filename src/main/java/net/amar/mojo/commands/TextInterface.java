package net.amar.mojo.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TextInterface{
 String getName();
 void executeMsg(MessageReceivedEvent event, String[] args);
}