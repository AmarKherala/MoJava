package net.amar.mojava.events;

import net.amar.mojava.Load;
import net.amar.mojava.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;

public class MemberPunished extends ListenerAdapter{
    static String logChannelId = Load.getLogChannelId();
    static String verdictChannelId = Load.getVerdictChannelId();
    static String targetM = "";
    static String targetId = "";
    static String reason = "";
    static String duration = "";
    static String mod = "";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannel().getId().equals(logChannelId)) return;

        TextChannel verdict =verdictChannelId!=null ? event.getGuild().getTextChannelById(verdictChannelId) : null;
        Message msg = event.getMessage();

        if (!msg.getEmbeds().isEmpty()) {
            MessageEmbed em = msg.getEmbeds().getFirst();
            if (em.getTitle() != null) {
                if (em.getTitle().startsWith("timeout | case") ||
                    em.getTitle().startsWith("warn | case") ||
                    em.getTitle().startsWith("ban | case") ||
                    em.getTitle().startsWith("unban | case") ||
                    em.getTitle().startsWith("kick | case") ||
                    em.getTitle().startsWith("tempban | case")){

                    if (em.getDescription()==null) {
                        Log.warn("embed description from message ["+em+"] is null and will be ignored");
                        return;
                    }
                    String[] des = em.getDescription().split("\n");

                    targetM = "";
                    targetId = "";
                    reason = "";
                    duration = "";
                    mod = "";

                    for (String line : des) {
                        if (line.startsWith("**Offender:**")) {
                            targetM = line.substring(line.indexOf("<@"));
                            targetId = targetM.replaceAll("[^0-9]", "");
                        }
                        if (line.startsWith("**Reason:**")) {
                            reason = line.substring("**Reason:**".length()).trim();
                        }
                        if (line.startsWith("**Duration:**")) {
                            duration = line.substring("**Duration:**".length()).trim();
                        }
                        if (line.startsWith("**Responsible moderator:**")) {
                            mod = line.substring("**Responsible moderator:**".length()).trim();
                        }
                    }
                    event.getGuild().getJDA().retrieveUserById(targetId).queue(target -> {

                        if (verdict == null) {
                            Log.warn("verdict channel is null, cant send verdict message");
                            return;
                        }

                        if (em.getTitle().startsWith("timeout | case"))
                            verdict.sendMessageEmbeds(timeOut(target).build()).queue();

                        else if (em.getTitle().startsWith("warn | case"))
                            verdict.sendMessageEmbeds(warn(target).build()).queue();

                        else if (em.getTitle().startsWith("kick | case"))
                            verdict.sendMessageEmbeds(kick(target).build()).queue();

                        else if (em.getTitle().startsWith("ban | case"))
                            verdict.sendMessageEmbeds(ban(target).build()).queue();

                        else if (em.getTitle().startsWith("unban | case"))
                            verdict.sendMessageEmbeds(unban(target).build()).queue();
                        else if (em.getTitle().startsWith("tempban | case"))
                            verdict.sendMessageEmbeds(tempBan(target).build()).queue();
                    });
                }
            }
        }
    }


    private EmbedBuilder timeOut(User target) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Member muted!");
        em.setThumbnail(target.getAvatarUrl());
        em.setDescription("**Moderator**:\n" + mod
                + "\n**Target**:\n" + target.getAsMention()
                + "\n**Target ID**:\n" + target.getId()
                + "\n**Reason**:\n" + reason
                + "\n**Duration**:\n" + duration);
        em.setColor(Color.RED);
        em.setTimestamp(OffsetDateTime.now());
        return em;
    }

    private EmbedBuilder warn(User target) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Member warned!");
        em.setThumbnail(target.getAvatarUrl());
        em.setDescription("**Moderator**:\n" + mod
                + "\n**Target**:\n" + target.getAsMention()
                + "\n**Target ID**:\n" + target.getId()
                + "\n**Reason**:\n" + reason);
        em.setColor(Color.RED);
        em.setTimestamp(OffsetDateTime.now());
        return em;
    }

    private EmbedBuilder kick(User target) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Member kicked!");
        em.setThumbnail(target.getAvatarUrl());
        em.setDescription("**Moderator**:\n" + mod
                + "\n**Target**:\n" + target.getAsMention()
                + "\n**Target ID**:\n" + target.getId()
                + "\n**Reason**:\n" + reason);
        em.setColor(Color.RED);
        em.setTimestamp(OffsetDateTime.now());
        return em;
    }

    private EmbedBuilder ban(User target) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Member banned!");
        em.setThumbnail(target.getAvatarUrl());
        em.setDescription("**Moderator**:\n" + mod
                + "\n**Target**:\n" + target.getAsMention()
                + "\n**Target ID**:\n" + target.getId()
                + "\n**Reason**:\n" + reason);
        em.setColor(Color.RED);
        em.setTimestamp(OffsetDateTime.now());
        return em;
    }

    private EmbedBuilder tempBan(User target){
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Member tempban!");
        em.setThumbnail(target.getAvatarUrl());
        em.setDescription("**Moderator**:\n" + mod
                + "\n**Target**:\n" + target.getAsMention()
                + "\n**Target ID**:\n" + target.getId()
                + "\n**Reason**:\n" + reason
                + "\n**Duration**:\n" + duration);
        em.setColor(Color.RED);
        em.setTimestamp(OffsetDateTime.now());
        return em;
    }

    private EmbedBuilder unban(User target) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Member unbanned!");
        em.setThumbnail(target.getAvatarUrl());
        em.setDescription("**Moderator**:\n" + mod
                + "\n**Target**:\n" + target.getAsMention()
                + "\n**Target ID**:\n" + target.getId()
                + "\n**Reason**:\n" + reason);
        em.setColor(Color.GREEN);
        em.setTimestamp(OffsetDateTime.now());
        return em;
    }
}
