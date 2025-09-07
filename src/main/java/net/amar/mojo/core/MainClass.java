package net.amar.mojo.core;

import java.util.EnumSet;

import net.amar.mojo.events.ThreadCreated;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MainClass {

    private static JDA jda;
    public static void main(String[] args) throws InterruptedException{

        String token = LoadData.BotToken();

        if(token.isEmpty() || token.isBlank()){
            AmarLogger.warn("Missing/Unable to read bot token at resources/.env, breaking..");
            return;
        }
        try{
            AmarLogger.info("Starting...");
        jda = JDABuilder.createDefault(token)
        .enableIntents(EnumSet.allOf(GatewayIntent.class))
        .addEventListeners(
         new ThreadCreated()
        )
        .build();
        } catch (Exception e){
            AmarLogger.error("Something went wrong at start-up, procces killed.",e);
        }
        jda.awaitReady();
    }

    public void commands(){
      jda.updateCommands()
      .addCommands(
        Commands.slash("mute","mute a bad guy")
        .addOption(OptionType.USER, "user", "who to mute")
        .addOption(OptionType.NUMBER, "duration", "for how long should they stay muted")
        .addOption(OptionType.STRING, "reason", "why mute this guy")
      );
    }
}
