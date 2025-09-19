package net.amar.mojo.commands.info;

import net.amar.mojo.commands.CmdInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HostInfo implements CmdInterface {

    @Override
    public String getName() {
    return "info-host";
    }

    @Override
    public String getDescription() {
    return "informatiob about the host";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
      long maxMem = Runtime.getRuntime().maxMemory() /1024/1024;
      long freeMem = Runtime.getRuntime().freeMemory() /1024/1024;
      EmbedBuilder em = new EmbedBuilder();
      em.setDescription("```js\nOS NAME       :"+System.getProperty("os.name")
                          +"\nOS VERSION    :"+System.getProperty("os.version")
                          +"\nVENDOR        :"+System.getProperty("java.vendor")
                          +"\nARCHITECTURE  :"+System.getProperty("os.arch")
                          +"\nJAVA VERSION  :"+System.getProperty("java.version")
                          +"\nMAX MEMORY    :"+maxMem+"MiB"
                          +"\nUSED MEMORY   :"+(maxMem-freeMem) +"MiB"
                          +"\n```"
      );
      event.replyEmbeds(em.build()).queue();
    }
}
