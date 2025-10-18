package net.amar;

import java.util.EnumSet;

import net.amar.events.ButtonClickEvent;
import net.amar.events.MemberPunished;
import net.amar.events.SupportThreads;
import net.amar.handler.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main{

    private static JDA jda;

    public static void main(String[] args) throws Exception{

        String token=Load.getBotToken();
        assert token!=null;
        if(token.isBlank()){
            Log.error("Could not load token from resources/data.json, make sure the file exist");
            return;
        }

        Log.info("Building JDA instance...");
        long startTimer = System.currentTimeMillis();
        try{
            jda=JDABuilder.createDefault(token)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
                    .setMemberCachePolicy(MemberCachePolicy.ONLINE)
                    .setStatus(OnlineStatus.IDLE)
                    .setActivity(Activity.customStatus(":D"))
                    .addEventListeners(
                            new SlashHandler() ,
                            new TextHandler() ,
                            new SupportThreads() ,
                            new MemberPunished() ,
                            new ButtonClickEvent()
                    )
                    .build();
        } catch(Exception e){
            Log.error("Failed to build Bot" , e);
        }



        long timeTaken = System.currentTimeMillis() - startTimer;
        Log.info("Building Bot instance took "+timeTaken+"ms");
        jda.awaitReady();

        if(Load.getGuildId()!=null){
            Guild guild=jda.getGuildById(Load.getGuildId());
            if(guild!=null) setMojoCommands(guild);
        }
    }

    private static void setMojoCommands(Guild guild){
        guild.updateCommands().addCommands(
                Commands.slash("info" , "info about the bot") ,
                Commands.slash("help" , "shows a list of commands") ,
                Commands.slash("mod-ban-list" , "displays the mods blacklist") ,
                Commands.slash("verify","gains you access to pre-testing chanel") ,

                Commands.slash("mod-ban-remove","remove a mod from mods blacklist")
                        .addOption(OptionType.STRING,"mod-id","the mod to remove", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)),
                                
                Commands.slash("mod-ban-support" , "adds a mod to the support blacklist")
                        .addOption(OptionType.STRING , "mod-id" , "the mod to blacklist", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)),


                Commands.slash("ban","ban a bad guy")
                        .addOption(OptionType.USER, "member", "who to ban", true)
                        .addOption(OptionType.STRING, "reason", "why ban this guy", true)
                        .addOption(OptionType.BOOLEAN, "appealable","should the ban be appealable", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))

        ).queue(
                success->Log.info("Loaded guild commands for guild "+guild.getName()+" successfully") ,
                failure->Log.error("Failed to load commands for guid :"+guild.getName())
        );
    }

    public static JDA getJDA() {
        return jda;
    }
}
