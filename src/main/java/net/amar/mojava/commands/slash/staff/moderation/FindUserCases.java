package net.amar.mojava.commands.slash.staff.moderation;

import net.amar.mojava.Main;
import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.slash.SlashCommand;
import net.amar.mojava.db.DBCaseFinder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class FindUserCases implements SlashCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "find-user-cases";
    }

    @Override
    public String getDescription() {
        return "get all cases of a user";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        User u = event.getOption("user").getAsUser();
        TextChannel tc = event.getChannel().asTextChannel();
        User req = event.getUser();
        event.replyFormat("getting cases for %s", u.getName()).setEphemeral(true).queue();
        DBCaseFinder.findUserCases(u, req, tc, Main.waiter);
    }
}
