package net.amar.mojo.commands.util;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.amar.mojo.commands.CmdInterface;
import net.amar.mojo.core.LoadData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
@SuppressWarnings("null")
public class modping implements CmdInterface {
    static String mod = LoadData.modRole();
    private final Map<String, Long> lastUsage = new HashMap<>();
    @Override
    public String getName() {
       return "modping";
    }

    @Override
    public String getDescription() {
       return "only use when necessary";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        long now = System.currentTimeMillis();

        if (lastUsage.containsKey(userId)){
            long last = lastUsage.get(userId);
            long diff = now - last;
            if (diff < 10*60*1000){
                event.reply("""
                    You can only use modping once each 10 minutes, this happens to prevent spam.
                      **IF YOUR MODPING WAS USELESS YOU WILL GET MUTED FOR AT LEAST 5 DAYS!**
                """).setEphemeral(true).queue();
                return;
            }
        }
        lastUsage.put(userId, now);
        Role modss = event.getGuild().getRoleById(mod);
        List<Member> mods = event.getGuild().getMembersWithRoles(modss);
        
        Collections.shuffle(mods);
        int count = Math.min(4, mods.size());
        List<Member> theMods = mods.subList(0, count);
        StringBuilder pings = new StringBuilder();
        for (Member m: theMods){
            pings.append(m.getAsMention()).append(" ");
        }
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Mod ping");
        em.setDescription("**NOTE TO MODS**:\nIf the modping was done for no purpose, feel free to punsish the user.");
        em.setColor(Color.RED);
        em.setFooter("Requested by: "+event.getUser().getName(), event.getUser().getAvatarUrl());
        em.setTimestamp(OffsetDateTime.now());
        event.reply(pings.toString().trim()).queue();
        event.getChannel().sendMessageEmbeds(em.build()).queue();
    }
    
}