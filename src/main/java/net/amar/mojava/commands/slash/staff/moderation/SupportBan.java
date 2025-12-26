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

public class SupportBan implements SlashCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "support-ban";
    }

    @Override
    public String getDescription() {
        return "ban someone from getting support";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        User u = event.getOption("user").getAsUser();
        String reason = event.getOption("reason").getAsString();
        Boolean appeal = event.getOption("appeal").getAsBoolean();
        Message.Attachment image = null;
        Guild g = event.getGuild();
        TextChannel tc = g.getTextChannelById(Load.getVerdictChannelId());

        if (event.getOption("proof")!=null) image = event.getOption("proof").getAsAttachment();

        long caseId = DBInsertCase.supportBan(
                u.getName(),
                u.getId(),
                event.getUser().getName(),
                event.getUser().getId(),
                reason,
                appeal
        );

        if (image==null) {
            NotifyCaseHandler.notifySupportBan(
                    tc,
                    u,
                    caseId,
                    event.getUser().getName(),
                    event.getUser().getId(),
                    reason,
                    null,
                    String.valueOf(appeal)
            ).thenRun(g.addRoleToMember(u, g.getRoleById(Load.getSupportBannedRoleId()))::queue);
            event.replyFormat("Support banned %s",u.getName()).queue();
            return;
        }
        NotifyCaseHandler.notifySupportBan(
                tc,
                u,
                caseId,
                event.getUser().getName(),
                event.getUser().getId(),
                reason,
                image.getUrl(),
                String.valueOf(appeal)
        ).thenRun(g.addRoleToMember(u, g.getRoleById(Load.getSupportBannedRoleId()))::queue);
        event.replyFormat("Support banned %s",u.getName()).queue();
    }
}
