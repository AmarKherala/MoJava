package net.amar.mojava.commands.slash.staff.moderation;

import net.amar.mojava.Load;
import net.amar.mojava.Log;
import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.slash.SlashCommand;
import net.amar.mojava.db.DBInsertCase;
import net.amar.mojava.handler.NotifyCaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.TimeUnit;

public class Mute implements SlashCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "mute a bad person";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {

        Message.Attachment image = null;

        if (event.getOption("proof")!=null) {
            image = event.getOption("proof").getAsAttachment();
        }

        User u = event.getOption("user").getAsUser();
        String dur = event.getOption("duration").getAsString();
        String reason = event.getOption("reason").getAsString();

        int amount = Integer.parseInt(dur.replaceAll("\\D+", ""));

        Guild g = event.getGuild();
        TextChannel tc = g.getTextChannelById(Load.getVerdictChannelId());

        try {
            TimeUnit unit = parseTimeUnit(dur);
            if (dur.toLowerCase().contains("w")) amount*=7;

            long caseId = DBInsertCase.timeout(
                    u.getName(),
                    u.getId(),
                    event.getUser().getName(),
                    event.getUser().getId(),
                    reason,
                    dur
            );

            if (image!=null) {
                NotifyCaseHandler.notifyMute(
                        tc,
                        u,
                        caseId,
                        event.getUser().getName(),
                        event.getUser().getId(),
                        reason,
                        image.getUrl(),
                        dur).thenRun(g.timeoutFor(u, amount, unit)::queue);
            } else {
                NotifyCaseHandler.notifyMute(
                        tc,
                        u,
                        caseId,
                        event.getUser().getName(),
                        event.getUser().getId(),
                        reason,
                        null,
                        dur).thenRun(g.timeoutFor(u, amount, unit)::queue);
            }
            event.replyFormat("Muted **%s** for **%s**", u.getName(), dur).queue();
        } catch (Exception e) {
            event.reply("something went wrong").setEphemeral(true).queue();
            Log.error("failure while trying to mute a person",e);
        }
    }

    private static TimeUnit parseTimeUnit(String duration) {
        if (duration == null || duration.isBlank())
            throw new IllegalArgumentException("Duration is empty");

        String unit = duration.replaceAll("\\d+", "").toLowerCase();

        return switch (unit) {
            case "w", "week", "weeks" -> TimeUnit.DAYS;   // handle weeks elsewhere (×7)
            case "d", "day", "days"   -> TimeUnit.DAYS;
            case "h", "hour", "hours" -> TimeUnit.HOURS;
            case "m", "min", "minute", "minutes" -> TimeUnit.MINUTES;
            case "s", "sec", "second", "seconds" -> TimeUnit.SECONDS;
            default -> throw new IllegalArgumentException("Invalid time unit: " + unit);
        };
    }
}
