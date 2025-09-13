/*
 * This class is only meant to detect support banned/banned
 * members and try to dm them the appeal link.
 *  - amarDev();
 */
package net.amar.mojo.events;

import java.awt.Color;
import java.time.Duration;
import java.time.OffsetDateTime;

import net.amar.mojo.core.AmarLogger;
import net.amar.mojo.core.LoadData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateTimeOutEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberPubished extends ListenerAdapter {
  // member bans
  @Override
  @SuppressWarnings("null")
  public void onGuildBan(GuildBanEvent event) {
    Guild mojoGuild = event.getGuild();
    TextChannel verdict = mojoGuild.getTextChannelById(LoadData.verdictChannel());
    mojoGuild.retrieveAuditLogs().type(ActionType.BAN).limit(1).queue(
        ent -> {
          if (!ent.isEmpty()) {
            AuditLogEntry entry = ent.get(0);

            long targetId = entry.getTargetIdLong();
            String reason = entry.getReason();

            mojoGuild.getJDA().retrieveUserById(targetId).queue(
                target -> {
                  String name = target.getAsMention();
                  EmbedBuilder ban = new EmbedBuilder();
                  ban.setTitle("Member banned");
                  ban.setThumbnail(target.getAvatarUrl());
                  ban.setDescription(
                      "### Target: \n" + name + "\n### Target ID: \n" + targetId + "\n### Reason: \n" + reason);
                  ban.setColor(Color.RED);
                  ban.setTimestamp(OffsetDateTime.now());
                  try {
                    verdict.sendMessageEmbeds(ban.build()).queue();
                  } catch (Exception e) {
                    AmarLogger.error("Something went wrong while sending verdict message..", e);
                  }
                });

          }
        });
  }

  // member unbans
  @Override
  @SuppressWarnings("null")
  public void onGuildUnban(GuildUnbanEvent event) {
    Guild mojoGuild = event.getGuild();
    TextChannel verdict = mojoGuild.getTextChannelById(LoadData.verdictChannel());
    mojoGuild.retrieveAuditLogs()
        .type(ActionType.UNBAN)
        .limit(1)
        .queue(ent -> {
          AuditLogEntry init = ent.get(0);
          String reason = init.getReason();
          long id = init.getIdLong();
          mojoGuild.getJDA().retrieveUserById(id).queue(target -> {
            String name = target.getAsMention();
            try {
              EmbedBuilder e = new EmbedBuilder();
              e.setTitle("Member unbanned!");
              e.setThumbnail(target.getAvatarUrl());
              e.setDescription("### Target:\n" + name + "\n### Target ID:\n" + id + "\n### Reason:\n" + reason);
              e.setColor(Color.GREEN);
              e.setTimestamp(OffsetDateTime.now());
              verdict.sendMessageEmbeds(e.build()).queue();
            } catch (Exception e) {
              AmarLogger.error("Something went wrong while sending unban verdict...", e);
            }
          });
        });
  }

  // member kicks
  @Override
  @SuppressWarnings("null")
  public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
    Guild mojoGuild = event.getGuild();
    TextChannel verdict = mojoGuild.getTextChannelById(LoadData.verdictChannel());
    mojoGuild.retrieveAuditLogs()
        .type(ActionType.KICK)
        .limit(1)
        .queue(ent -> {
          AuditLogEntry init = ent.get(0);
          if (!ent.isEmpty()) {
            long id = init.getTargetIdLong();
            String reason = init.getReason();
            User u = init.getUser();
            if (!u.getId().equals("235148962103951360")) {
              return;
            }
            if (init.getTargetIdLong() != event.getUser().getIdLong()) {
              return;
            }
            mojoGuild.getJDA().retrieveUserById(id).queue(target -> {
              String tar = target.getAsMention();
              EmbedBuilder kick = new EmbedBuilder();
              kick.setTitle("Member kicked!");
              kick.setThumbnail(target.getAvatarUrl());
              kick.setDescription("### Target: \n" + tar + "\n### Target ID: \n" + id + "\n### Reason: \n" + reason);
              kick.setColor(Color.RED);
              kick.setTimestamp(OffsetDateTime.now());
              try {
                verdict.sendMessageEmbeds(kick.build()).queue();
              } catch (Exception e) {
                AmarLogger.error("Somethimg went wrong while kicking", e);
              }
            });
          }
        });
  }

  // member timeouts
  @Override
  @SuppressWarnings("null")
  public void onGuildMemberUpdateTimeOut(GuildMemberUpdateTimeOutEvent event) {
    Guild mojoGuild = event.getGuild();
    TextChannel verdict = mojoGuild.getTextChannelById(LoadData.verdictChannel());

    OffsetDateTime until = event.getNewTimeOutEnd();
    if (until == null) {
      return;
    }
    OffsetDateTime now = OffsetDateTime.now();
    if (until.isAfter(now)) {
      Duration dur = Duration.between(now.toInstant(), until.toInstant());
      long totalMinutes = dur.toMinutes();

      String formattedDuration;
      if (totalMinutes >= 1440) {
        long days = totalMinutes / 1440;
        formattedDuration = days + "d";
      } else if (totalMinutes >= 60) {
        long hours = totalMinutes / 60;
        formattedDuration = hours + "h";
      } else {
        formattedDuration = totalMinutes + "m";
      }
      AmarLogger.info("Time for timeout to end :"+formattedDuration);
      mojoGuild.retrieveAuditLogs()
          .type(ActionType.MEMBER_UPDATE)
          .limit(1)
          .queue(ent -> {
            if (ent.isEmpty()) {
              return;
            }

            AuditLogEntry log = ent.get(0);
            if (log.getChangeByKey("communication_disabled_until") == null) {
              return;
            }

            String reason = log.getReason();
            long targetId = log.getTargetIdLong();

            mojoGuild.getJDA().retrieveUserById(targetId).queue(target -> {
              try {
                EmbedBuilder b = new EmbedBuilder();
                b.setTitle("Member muted!");
                b.setThumbnail(target.getAvatarUrl());
                b.setDescription("### Target:\n" + target.getAsMention()
                    + "\n### Target ID:\n" + targetId
                    + "\n### Reason:\n" + reason
                    + "\n### Duration:\n" + formattedDuration);
                b.setColor(Color.RED);
                b.setTimestamp(OffsetDateTime.now());
                verdict.sendMessageEmbeds(b.build()).queue();
              } catch (Exception e) {
                AmarLogger.error("Error sending timeout verdict", e);
              }
            });
          });
    }
  }

  // support banned role detection
  @Override
  @SuppressWarnings("null")
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
      AmarLogger.error("Something went wrong. maybe missing perms?", e);
    }
  }
}
