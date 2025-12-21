package net.amar.mojava.commands.text;

import net.amar.mojava.commands.CommandCategories;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TextCommand {

  CommandCategories getCategory();

  String getName();

  String getDescription();

  void executeMessage(MessageReceivedEvent event, String[] args);
}
