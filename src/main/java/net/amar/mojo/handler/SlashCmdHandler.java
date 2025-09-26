package net.amar.mojo.handler;

import java.util.HashMap;

import net.amar.mojo.commands.SlashCommand;
import net.amar.mojo.commands.slash.info.BotInfo;
import net.amar.mojo.commands.slash.info.HostInfo;
import net.amar.mojo.commands.slash.mods.BanList;
import net.amar.mojo.commands.slash.mods.BanMod;
import net.amar.mojo.commands.slash.mods.UnBanMod;
import net.amar.mojo.commands.slash.util.ControlsOpacity;
import net.amar.mojo.commands.slash.util.Modping;
import net.amar.mojo.commands.slash.util.ModpingEdit;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCmdHandler extends ListenerAdapter {
    
    private static final HashMap <String, SlashCommand> commands = new HashMap<>();

    public SlashCmdHandler(){
     commands.put("info", (SlashCommand) new BotInfo());
     commands.put("info-host", (SlashCommand) new HostInfo());
     commands.put("ban-mod", (SlashCommand) new BanMod());
     commands.put("mod-list",(SlashCommand) new BanList());
     commands.put("unban-mod",(SlashCommand) new UnBanMod());
     commands.put("modping",(SlashCommand) new Modping());
     commands.put("modping-edit",(SlashCommand) new ModpingEdit());
     commands.put("set-opacity", (SlashCommand) new ControlsOpacity());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String CmdName = event.getName();
        SlashCommand cmd = commands.get(CmdName);
        if (cmd!=null) {
            cmd.executeSlash(event);
        }
    }
}
