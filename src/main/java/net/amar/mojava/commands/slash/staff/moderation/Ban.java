package net.amar.mojava.commands.slash.staff.moderation;

import net.amar.mojava.Load;
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

public class Ban implements SlashCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "ban a bad user";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        User user = event.getOption("user").getAsUser();
        String reason = event.getOption("reason").getAsString();
        Boolean appeal = event.getOption("appeaable").getAsBoolean();
        Message.Attachment image;
        Guild guild = event.getGuild();
        TextChannel tc = guild.getTextChannelById(Load.getVerdictChannelId());

        long caseId = DBInsertCase.ban(user.getName(),
                user.getId(),
                event.getUser().getName(),
                event.getUser().getId(),
                reason, appeal);

        if (event.getOption("proof")!=null) {
            image = event.getOption("proof").getAsAttachment();
            NotifyCaseHandler.notifyBan(
                    tc,
                    user,
                    caseId,
                    event.getUser().getName(),
                    event.getUser().getId(),
                    reason,
                    String.valueOf(appeal)
                    ,image.getUrl()
            ).thenRun(guild.ban(user, 0, TimeUnit.DAYS).reason(reason)::queue);

        } else {
            NotifyCaseHandler.notifyBan(
                    tc,
                    user,
                    caseId,
                    event.getUser().getName(),
                    event.getUser().getId(),
                    reason,
                    String.valueOf(appeal)
                    ,null
            ).thenRun(guild.ban(user, 0, TimeUnit.DAYS).reason(reason)::queue);
        }
        event.reply("banned "+user.getName()).queue();
    }
}
