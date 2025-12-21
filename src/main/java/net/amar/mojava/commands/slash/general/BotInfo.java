package net.amar.mojava.commands.slash.general;

import net.amar.mojava.commands.CommandCategories;
import net.amar.mojava.commands.slash.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;
import java.time.OffsetDateTime;

public class BotInfo implements SlashCommand {

  @Override
  public CommandCategories getCategory() {
    return CommandCategories.GENERAL;
  }

	@Override
	public String getName() {
   return "info";
	}

	@Override
	public String getDescription() {
	 return "some information about the bot";
	}

	@Override
	public void executeSlash(SlashCommandInteractionEvent event) {
		User b = event.getJDA().getSelfUser();
		User u = event.getUser();
		EmbedBuilder em = new EmbedBuilder();

		em.setTitle("Information about me");
		em.setThumbnail(b.getAvatarUrl());
		em.setColor(Color.CYAN);
		em.setFooter("Requested by: "+u.getAsMention(), u.getAvatarUrl());
		em.setTimestamp(OffsetDateTime.now());
		em.setDescription("### Introduction:\nI'm a bot made with Java using [JDA](https://github.com/discord-jda/JDA) libary by amaroreo.\nMy soul purpose is to automate and help with the support system in MojoLauncher.en server, plus some extra features my creature wished to add!\nIf you wish to see my code or help my creator improve me, you can do that by clicking the button below!\n### PING: "+event.getJDA().getGatewayPing()+"ms");

		Button button = Button.link("https://github.com/AmarKherala/MoJava", Emoji.fromUnicode("📄"));

		event.replyEmbeds(em.build()).addActionRow(button).queue();
	}
}
