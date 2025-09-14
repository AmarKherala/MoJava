package net.amar.mojo.core;

import java.util.EnumSet;

import net.amar.mojo.events.ExecCmd;
import net.amar.mojo.events.MemberPubished;
import net.amar.mojo.events.SupportPosts;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

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
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableIntents(EnumSet.allOf(GatewayIntent.class))
        .addEventListeners(
         new SupportPosts(),
         new MemberPubished(),
         new ExecCmd()
        )
        .build();
        } catch (Exception e){
            AmarLogger.error("Something went wrong at start-up, procces killed.",e);
        }
        jda.awaitReady();
        addCommands();
    }
    static void addCommands(){
   jda.updateCommands()
   .addCommands(
    Commands.slash("info","information about the bot")
   ).queue(
    success -> AmarLogger.info("successfully added commands"),
    failure -> AmarLogger.warn("couldnt add commands")
   );
    }
}
