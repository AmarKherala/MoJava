package net.amar.mojava;

import java.util.EnumSet;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.amar.mojava.db.DBManager;
import net.amar.mojava.events.ButtonClickEvent;
import net.amar.mojava.events.MemberPunished;
import net.amar.mojava.events.SupportThreads;
import net.amar.mojava.handler.*;

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
    public static EventWaiter waiter = new EventWaiter();
    public static void main(String[] args) throws Exception{

        String token= Load.getBotToken()!=null ? Load.getBotToken() : null;

       if(token != null && token.isEmpty()){
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
                            new ButtonClickEvent(),
                            waiter
                    )
                    .build();
        } catch(Exception e){
            Log.error("Failed to build Bot" , e);
        }
        DBManager.initDB();
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

                // moderation cmd here
                Commands.slash("ban","ban a bad user")
                        .addOption(OptionType.USER, "user","user to ban", true)
                        .addOption(OptionType.STRING,"reason","why ban this user",true)
                        .addOption(OptionType.BOOLEAN,"appeaable","can they appeal", true)
                        .addOption(OptionType.ATTACHMENT,"proof","optional proof", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

                Commands.slash("find-case", "find a case by id")
                        .addOption(OptionType.INTEGER, "id", "case id", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

                Commands.slash("find-user-cases","find all cases of a user")
                        .addOption(OptionType.USER, "user", "user to get cases of",true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

                Commands.slash("unban", "unban a user")
                        .addOption(OptionType.STRING, "uid", "user to unban", true)
                        .addOption(OptionType.STRING, "reason", "why unban this user", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

                Commands.slash("mute","mute or timeout a bad guy")
                        .addOption(OptionType.USER, "user", "person to mute", true)
                        .addOption(OptionType.STRING, "duration", "how long should they be muted", true)
                        .addOption(OptionType.STRING, "reason", "why mute this person", true)
                        .addOption(OptionType.ATTACHMENT,"proof","optional proof", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

                Commands.slash("support-ban", "ban someone from getting support")
                        .addOption(OptionType.USER, "user", "user to ban from support",true)
                        .addOption(OptionType.STRING, "reason", "why support ban them",true)
                        .addOption(OptionType.BOOLEAN, "appeal","can they appeal",true)
                        .addOption(OptionType.ATTACHMENT, "proof", "optional proof",false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

                Commands.slash("warn", "warn a person")
                        .addOption(OptionType.USER, "user", "person to warn", true)
                        .addOption(OptionType.STRING, "reason", "ehy warn this person", true)
                        .addOption(OptionType.ATTACHMENT, "proof", "optional proof",false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),

                Commands.slash("kick", "kick a person")
                        .addOption(OptionType.USER, "user", "person to warn", true)
                        .addOption(OptionType.STRING, "reason", "ehy warn this person", true)
                        .addOption(OptionType.ATTACHMENT, "proof", "optional proof",false)
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
