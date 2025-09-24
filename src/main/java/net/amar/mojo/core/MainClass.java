package net.amar.mojo.core;

import java.util.EnumSet;

import net.amar.mojo.events.ExecCmd;
import net.amar.mojo.events.MemberPubished;
import net.amar.mojo.events.SupportPosts;
import net.amar.mojo.txtcommands.TxtHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
public class MainClass {

    private static JDA jda;

    public static void main(String[] args) throws InterruptedException {

        String token = LoadData.BotToken();
        long mojoGuildId = 1365346109131722753L;

        if (token.isEmpty() || token.isBlank()) {
            AmarLogger.warn("Missing/Unable to read bot token at resources/.env, breaking..");
            return;
        }
        try {
            AmarLogger.info("Starting...");
            jda = JDABuilder.createDefault(token)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
		            .setActivity(Activity.watching("The support threads"))
                    .addEventListeners(
                            new SupportPosts(),
                            new MemberPubished(),
                            new ExecCmd(),
                            new TxtHandler()
                            )
                    .build();
        } catch (Exception e) {
            AmarLogger.error("Something went wrong at start-up, processes killed.", e);
        }
        jda.awaitReady();
        Guild mojo = jda.getGuildById(mojoGuildId);
        if (mojo != null) {
            // mojo only commands
            mojo.updateCommands().addCommands(
                    // mod ban/unban/list commands
                    Commands.slash("ban-mod", "add a mod to the ban list")
                            .addOption(OptionType.STRING, "mod-id", "the id of the mod")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor( Permission.MODERATE_MEMBERS)),

                    Commands.slash("unban-mod","remove a mod from ban list")
                    .addOption(OptionType.STRING,"mod-id","mod id to remove from ban list")
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)),
                                    
                    Commands.slash("mod-list", "banned mods list")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS)),
                    // modping, modping-edit ,modping-list
                    Commands.slash("modping","only use when necessary"),
                    
                    Commands.slash("modping-edit", "add-remove a role to modping")
                    .addOption(OptionType.ROLE, "role", "role to add-remove",true)
                    .addOptions( new OptionData(OptionType.STRING,"edit","remove or add",true)
                    .addChoice("remove", "remove")
                    .addChoice("add", "add"))
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                    )
                    .queue(
                            success -> AmarLogger.info("successfully added mojo server commands"),
                            failure -> AmarLogger.warn("couldn't add mojo server commands"));
        } else {
            AmarLogger.warn("Couldnt find mojo server...");
        }
        addCommands();
    }

    static void addCommands() {
        jda.updateCommands()
                .addCommands(
                        Commands.slash("info", "information about the bot"),
                        Commands.slash("info-host","informatiob about the host"))
                .queue(
                        success -> AmarLogger.info("successfully added commands"),
                        failure -> AmarLogger.warn("couldnt add commands"));
    }
}
