package net.amar.mojo.commands.text.info;

import net.amar.mojo.commands.TextInterface;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help implements TextInterface{

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void executeMsg(MessageReceivedEvent event, String[] args) {
       event.getMessage().reply("""
            *Current bot commands*:
            - fun ;
            ``m!mopding`` fake modping

            -info ;
            ``m!ping`` bot ping
        """).queue();
    }
    
}
