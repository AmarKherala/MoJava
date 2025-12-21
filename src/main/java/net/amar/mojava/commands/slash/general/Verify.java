package net.amar.mojava.commands.slash.general;

import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.slash.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Verify implements SlashCommand {

    @Override
    public CommandCategories getCategory() {
        return CommandCategories.GENERAL;
    }

    @Override
    public String getName() {
        return "verify";
    }

    @Override
    public String getDescription() {
        return "gains you access to #pre-testing channel";
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {

        event.reply("""
                By clicking the button below you agree to follow these rules:
                1. You agree to not go offtopic in the channel.
                2. You agree to only text there when the dev's ping the role.
                Any violation to those points will immediately result in a 7d timeout.
                """).addActionRow(
                Button.success("verify" , "VERIFY")
        ).setEphemeral(true).queue();
    }
}
