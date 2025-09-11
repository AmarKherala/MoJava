package net.amar.mojo.events;

import java.awt.Color;

import net.amar.mojo.core.AmarLogger;
import net.amar.mojo.core.LoadData;
import net.amar.mojo.handler.RequestsHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ThreadCreated extends ListenerAdapter {

    static String mod = LoadData.modRoleId();
    static String admin = LoadData.adminRoleId();

    static String launcherVersion = "";
    static String deviceModel = "";
    static String mojoRenderer = "";
    static String graphicsDevice = "";
    static String minecraftVersion = "";
    static String javaVersion = "";

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        String ch = LoadData.forumChannelId();
        if (!event.getChannelType().isThread())
            return;

        ThreadChannel Thread = event.getChannel().asThreadChannel();

        if (Thread.getParentChannel() instanceof ForumChannel forum && forum.getId().equals(ch)) {
            Thread.retrieveStartMessage().queue(starter -> {
                boolean hasLog = starter.getAttachments()
                        .stream()
                        .anyMatch(
                                att -> att.getFileName().contains("latestlog") || att.getFileName().contains(("log")));
                if (!hasLog) {
                    EmbedBuilder noLog = new EmbedBuilder();
                    noLog.setTitle("NO LOG!");
                    noLog.setDescription(
                            "You didn't provide a log file, to get help please read [this](https://discord.com/channels/1365346109131722753/1390045622924738651/1390045622924738651).\nYou can ignore the message if the log wasn't attached intentionally or if you're using a random file name generator plugin.\n### Note that we won't help you with anything else other than a problem in the functionality of Mojo.\n-# click on the blue text");
                    noLog.setColor(Color.RED);
                    starter.replyEmbeds(noLog.build()).queue();
                    return;
                }
                handleLogMessage(starter);
                return;
            });
        }
    }

   @Override
public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot())
        return;

    if (event.getChannelType().isThread()) {
        ThreadChannel thread = event.getChannel().asThreadChannel();
        if (thread.getParentChannel() instanceof ForumChannel forum
                && forum.getId().equals(LoadData.forumChannelId())) {
            
            handleLogMessage(event.getMessage());

            
            event.getGuild().retrieveMember(event.getAuthor()).queue(userW -> {
                boolean isUserStaff = userW.getRoles().stream()
                        .anyMatch(role -> role.getId().equals(mod) || role.getId().equals(admin));

                if (isUserStaff)
                    return; // staff can ping freely

                for (Member m : event.getMessage().getMentions().getMembers()) {
                    if (event.getMessage().getReferencedMessage() != null &&
                            m.getId().equals(event.getMessage().getReferencedMessage().getAuthor().getId())) {
                        continue; // don’t warn if it’s a reply
                    }

                    boolean isStaff = m.getRoles().stream()
                            .anyMatch(role -> role.getId().equals(admin) || role.getId().equals(mod));
                    if (isStaff) {
                        event.getMessage().reply("""
                                                 Please don't ping any staffs for help.
                                                 Be patient, someone will help you when they can!
                                                 
                                                 **If you continue doing it you WILL get punished.**
                                                 **Note that we are NOT paid team!**""").queue();
                        break;
                    }
                }
            }, failure -> {
                AmarLogger.error("Failed to retrieve member for user " 
                        + event.getAuthor().getId() 
                        + " in guild " + event.getGuild().getId(), failure);
            });
        }
    }
}


    private void handleLogMessage(Message message) {

        Message.Attachment log = message.getAttachments()
                .stream()
                .filter(att -> att.getFileName().contains("log"))
                .findFirst()
                .orElse(null);

        if (log == null)
            return;

        String logContent = RequestsHandler.fetchLog(log.getUrl());
        if (logContent == null) {
            message.reply("Failed to fetch the log you provided").queue();
            AmarLogger.warn("Failed to fetch log");
            return;
        }

        String[] lines = logContent.split("\n");

        if (!logContent.contains("git.artdeell.mojo")) {
            message.replyEmbeds(failedLogParser().build()).queue();
            return;
        }

        launcherVersion = "";
        deviceModel = "";
        mojoRenderer = "";
        graphicsDevice = "";
        minecraftVersion = "";
        javaVersion = "";

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Info: Launcher version:")) {
                launcherVersion = line.substring("Info: Launcher version:".length()).trim();

            }

            if (line.startsWith("Info: Device model:")) {
                deviceModel = line.substring("Info: Device model:".length()).trim();
            }

            if (line.startsWith("Info: Graphics device:")) {
                graphicsDevice = line.substring("Info: Graphics device:".length()).trim();
            }

            if (line.startsWith("Info: Selected Minecraft version:")) {
                minecraftVersion = line.substring("Info: Selected Minecraft version:".length()).trim();
            }

            if (line.startsWith("Added custom env: MOJO_RENDERER=")) {
                mojoRenderer = line.substring("Added custom env: MOJO_RENDERER=".length()).trim();
            }

            if (line.startsWith("Added custom env: JAVA_HOME=/data/user/0/git.artdeell.mojo/runtimes/")) {
                javaVersion = line
                        .substring("Added custom env: JAVA_HOME=/data/user/0/git.artdeell.mojo/runtimes/".length())
                        .trim();
            } 
            if (line.startsWith("Added custom env: JAVA_HOME=/data/user/0/git.artdeell.mojo.debug/runtimes/")) {
                javaVersion = line
                        .substring(
                                "Added custom env: JAVA_HOME=/data/user/0/git.artdeell.mojo.debug/runtimes/".length())
                        .trim();
            }
            break;
        }

        if (launcherVersion.isBlank() && deviceModel.isBlank()) {
           message.replyEmbeds(noInfo().build()).queue();
            return;
        } 
        message.replyEmbeds(
                successfulLoadParser(launcherVersion, deviceModel, graphicsDevice, mojoRenderer, minecraftVersion,
                        javaVersion)
                        .build())
                .queue(); 
                return;
    }

    public EmbedBuilder successfulLoadParser(String launcherVersion, String deviceModel, String graphicsDevice,
            String mojoRenderer, String minecraftVersion, String javaVersion) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("Log Information");
        em.setDescription("### Mojo version:\n" + launcherVersion +
                "\n### Device model:\n" + deviceModel +
                "\n### GPU:\n" + graphicsDevice +
                "\n### Mojo Renderer:\n" + mojoRenderer +
                "\n### Minecraft version:\n" + minecraftVersion +
                "\n### Java runtime version:\n" + javaVersion);
        em.setColor(Color.GREEN);
        return em;
    }

    public EmbedBuilder failedLogParser() {
        EmbedBuilder notMojo = new EmbedBuilder();
        notMojo.setTitle("Invalid Log");
        notMojo.setDescription("The log you provided Is NOT a Mojo log\n### Note that we don't provide support for other launchers/forks of mojo.");
        notMojo.setColor(Color.RED);
        return notMojo;
    }

    public EmbedBuilder noInfo() {
        EmbedBuilder notMojo = new EmbedBuilder();
        notMojo.setTitle("Invalid Log");
        notMojo.setDescription(
                "The log you provided does not include any information about your device and the launcher version, please send a valid log.");
        notMojo.setColor(Color.RED);
        return notMojo;
    }
}