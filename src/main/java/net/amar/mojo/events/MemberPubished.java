/*
 * This class reads the #log to detect punishments
 * via carl-bot embeds and send them to #verdict
 *  - amarDev();
 */
package net.amar.mojo.events;

import java.awt.Color;
import java.time.OffsetDateTime;

import org.jetbrains.annotations.NotNull;

import net.amar.mojo.core.AmarLogger;
import net.amar.mojo.core.LoadData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@SuppressWarnings("")
public class MemberPubished extends ListenerAdapter {

  static String targetM = "";
  static String targetId = "";
  static String reason = "";
  static String duration = "";
  static String mod = "";

  @Override
  @SuppressWarnings("UnnecessaryReturnStatement")
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    if (!event.getChannel().getId().equals(LoadData.logChannel()))
      return;
    Guild mojoGuild = event.getGuild();
    TextChannel verdict = mojoGuild.getTextChannelById(LoadData.verdictChannel());
    Message msg = event.getMessage();
    if (!msg.getEmbeds().isEmpty()) {
      MessageEmbed emb = msg.getEmbeds().get(0);
      if (emb.getTitle().startsWith("timeout | case") ||
          emb.getTitle().startsWith("warn | case") ||
          emb.getTitle().startsWith("kick | case") ||
          emb.getTitle().startsWith("ban | case") ||
          emb.getTitle().startsWith("unban | case") ||
          emb.getTitle().startsWith("tempban | case")) {

        String[] des = emb.getDescription().split("\n");

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
        mojoGuild.getJDA().retrieveUserById(targetId).queue(target -> {

          if (emb.getTitle().startsWith("timeout | case"))
            verdict.sendMessageEmbeds(timeOut(target).build()).queue();

          else if (emb.getTitle().startsWith("warn | case"))
            verdict.sendMessageEmbeds(warn(target).build()).queue();

          else if (emb.getTitle().startsWith("kick | case"))
            verdict.sendMessageEmbeds(kick(target).build()).queue();

          else if (emb.getTitle().startsWith("ban | case"))
            verdict.sendMessageEmbeds(ban(target).build()).queue();

          else if (emb.getTitle().startsWith("unban | case"))
            verdict.sendMessageEmbeds(unban(target).build()).queue();
          else if (emb.getTitle().startsWith("tempban | case"))
            verdict.sendMessageEmbeds(tempBan(target).build()).queue();
        });
      }
    }
  }

  // support banned role detection
  @Override
  @SuppressWarnings("")
  public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
    Member memberWhoGotRole = event.getMember();
    Guild mojoGuild = event.getGuild();
    try {
      TextChannel verdict = mojoGuild.getTextChannelById(LoadData.verdictChannel());
      if (memberWhoGotRole.getRoles().contains(mojoGuild.getRoleById(LoadData.supportBanned()))) {
        EmbedBuilder e = new EmbedBuilder();
        e.setTitle(memberWhoGotRole.getAsMention() + " got support banned!", memberWhoGotRole.getAvatarUrl());
        e.setDescription(
            "A member was support banned due to unacceptable behaviors.\nThey can appeal [here](https://forms.gle/2PB54RqZitH2FXaV7)!");
        e.setColor(Color.CYAN);
        e.setTimestamp(OffsetDateTime.now());

        verdict.sendMessageEmbeds(e.build()).queue();
      }
    } catch (Exception e) {
      AmarLogger.error("Failed to send verdict, might be missing perms.", e);
    }
  }

  /* Embeds for the sake of cleaning the code abit */
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
