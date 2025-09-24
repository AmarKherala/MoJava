package net.amar.mojo.events;

import java.util.HashMap;

import net.amar.mojo.commands.CmdInterface;
import net.amar.mojo.commands.info.BotInfo;
import net.amar.mojo.commands.info.HostInfo;
import net.amar.mojo.commands.mods.BanList;
import net.amar.mojo.commands.mods.BanMod;
import net.amar.mojo.commands.mods.UnBanMod;
import net.amar.mojo.commands.util.modping;
import net.amar.mojo.commands.util.modpingEdit;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ExecCmd extends ListenerAdapter {
    
    private static final HashMap <String, CmdInterface> commands = new HashMap<>();

    public ExecCmd(){
     commands.put("info", new BotInfo());
     commands.put("info-host", new HostInfo());
     commands.put("ban-mod", new BanMod());
     commands.put("mod-list", new BanList());
     commands.put("unban-mod", new UnBanMod());
     commands.put("modping", new modping());
     commands.put("modping-edit", new modpingEdit());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String CmdName = event.getName();
        CmdInterface cmd = commands.get(CmdName);
        if (cmd!=null) {
            cmd.executeSlash(event);
        }
    }
}
