package net.amar.mojo.commands.text.info;

import net.amar.mojo.commands.TextInterface;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.amar.mojo.handler.*;
public class Help implements TextInterface {
    @Override
    public String Catagory() {
      return "info";
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String Description() {
      return "display avilable bot commands";
    }

    @Override
    public void executeMsg(MessageReceivedEvent event, String[] args) {
       event.getMessage().replyFormat("**Available bot commands**\n\n- slash commands\n**info :**\n%s\n**util :**\n%s\n**mods :**\n%s\n\n- text commands\n**info :**\n%s\n**fun :**\n%s\n**staff :**\n%s", SlashCmdHandler.infoCommands(), SlashCmdHandler.util(), SlashCmdHandler.mods(), TextCmdHandler.infoCommands(), TextCmdHandler.funCommands(), TextCmdHandler.modCommands()).queue();
    }
    
}
