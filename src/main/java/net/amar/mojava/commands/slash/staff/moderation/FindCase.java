package net.amar.mojava.commands.slash.staff.moderation;

import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.slash.SlashCommand;
import net.amar.mojava.db.DBCaseFinder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class FindCase implements SlashCommand {
    @Override
    public CommandCategories getCategory() {
        return CommandCategories.STAFF;
    }

    @Override
    public String getName() {
        return "find-case";
    }

    @Override
    public String getDescription() {
        return "find a case by its's id";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        int caseId = event.getOption("id").getAsInt();
        event.replyEmbeds(DBCaseFinder.findCaseEmbed(caseId).build()).queue();
    }
}
