package net.amar.mojo.txtcommands.commands.fun;

import java.awt.Color;
import java.time.OffsetDateTime;

import net.amar.mojo.txtcommands.TxtInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
public class Mopding implements TxtInterface{

    @Override
    public String getName() {
        return "mopding";
    }

    @Override
    public void executeMsg(MessageReceivedEvent event, String[] args) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("MODPING!1!1");
        em.setDescription("MODS! if you see this *please* take action.\nWait til a moderator ~~doesnt~~ see this and ~~not~~ come to *help* you.");
        em.setColor(Color.RED);
        em.setFooter("Requsted by: "+event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
        em.setTimestamp(OffsetDateTime.now());
        event.getMessage().replyEmbeds(em.build()).queue();
    }
}
