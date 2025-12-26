package net.amar.mojava.commands.slash.staff.moderation;

import net.amar.mojava.Load;
import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.slash.SlashCommand;
import net.amar.mojava.db.DBInsertCase;
import net.amar.mojava.handler.NotifyCaseHandler;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Unban implements SlashCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "unban a banned user";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        String uid = event.getOption("uid").getAsString();
        String reason = event.getOption("reason").getAsString();
        TextChannel tc = event.getGuild().getTextChannelById(Load.getVerdictChannelId());

        event.getJDA().retrieveUserById(uid).queue(u->{

           long caseId = DBInsertCase.unban(
                    u.getName(),
                    u.getId(),
                    event.getUser().getName(),
                    event.getUser().getId(),
                    reason
            );

            NotifyCaseHandler.notifyUnBan(
                    tc, u, caseId,
                    event.getUser().getName(),
                    event.getUser().getId(),
                    reason, null
                    );

            event.getGuild().unban(u).reason(reason).queue(
                    success -> event.reply("Unbanned "+u.getName()).queue(),
                    failure -> event.replyFormat("failed to unban %s, [%s]",u.getName(), failure.getMessage()).queue()

            );
        });
    }
}
