package net.amar.mojava.commands.text.general;

import net.amar.mojava.Load;
import net.amar.mojava.Log;
import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.text.TextCommand;
import net.amar.mojava.handler.TextHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;
import java.util.stream.Collectors;

public class Help implements TextCommand{

    static final StringBuilder prettyCmdList = new StringBuilder();
    static final String prefix =Load.getBotPrefix();
    @Override
    public CommandCategories getCategory(){
        return CommandCategories.GENERAL;
    }

    @Override
    public String getName(){
        return "help";
    }

    @Override
    public String getDescription(){
        return "displays available text commands";
    }

    @Override
    public void executeMessage(MessageReceivedEvent event , String[] args){
        if (!prettyCmdList.isEmpty()) {
            event.getMessage().reply("**Text Commands**\n"+prettyCmdList+"-# for slash commands do /help").queue();
            Log.info("Using the already cached text commands list");
            return;
        }
        Map<String, TextCommand> cmds =TextHandler.getTextCmdMap();
        for (CommandCategories cat : CommandCategories.values()) {
            String list = cmds.values().stream()
                    .filter(cmd -> cmd.getCategory() == cat)
                    .map(cmd -> "`"+prefix + cmd.getName() + "` — " + cmd.getDescription())
                    .collect(Collectors.joining("\n"));

            if (!list.isEmpty()) {
                prettyCmdList.append("**").append(cat.name()).append(" Commands:**\n");
                prettyCmdList.append(list).append("\n\n");
            }
        }
        event.getMessage().reply("**Text Commands**\n"+prettyCmdList+"-# for slash commands do /help").queue();
    }
}
