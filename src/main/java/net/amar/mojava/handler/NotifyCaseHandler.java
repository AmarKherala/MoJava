package net.amar.mojava.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

public class NotifyCaseHandler {

    public static CompletableFuture<Void> notifyBan(TextChannel tc, User u, long id, String mod, String modId, String reason, String appeal, String proof) {
        tc.sendMessageEmbeds(ban(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, appeal, proof).build()).queue();
        return u.openPrivateChannel().submit()
                .thenCompose(p -> p.sendMessageEmbeds(ban(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, appeal, proof).build()).submit())
                .thenCompose(pp -> pp.reply("you can appeal [here](https://forms.gle/2PB54RqZitH2FXaV7) if your ban is appealable!").submit())
                .handle((s, t) -> null);
    }

    public static void notifyUnBan(TextChannel tc, User u, long id, String mod, String modId, String reason, String proof) {
        tc.sendMessageEmbeds(unban(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, proof).build()).queue();
    }

    public static CompletableFuture<Void> notifySupportBan(TextChannel tc, User u, long id, String mod, String modId, String reason, String proof, String appeal) {
        tc.sendMessageEmbeds(supportBan(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, appeal, proof).build()).queue();
        return u.openPrivateChannel().submit()
                .thenCompose(p -> p.sendMessageEmbeds(supportBan(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, appeal, proof).build()).submit())
                .thenCompose(pp -> pp.reply("you can appeal [here](https://forms.gle/2PB54RqZitH2FXaV7) if your support ban is appealable!").submit())
                .handle((s, t) -> null);
    }

    public static CompletableFuture<Void> notifyKick(TextChannel tc, User u, long id, String mod, String modId, String reason, String proof) {
        tc.sendMessageEmbeds(kick(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, proof).build()).queue();
        return u.openPrivateChannel().submit()
                .thenCompose(p -> p.sendMessageEmbeds(kick(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, proof).build()).submit())
                .handle((s, t) -> null);
    }

    public static void notifyWarn(TextChannel tc, User u, long id, String mod, String modId, String reason, String proof) {
        tc.sendMessageEmbeds(warn(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, proof).build()).queue();
         u.openPrivateChannel().queue(p -> p.sendMessageEmbeds(warn(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, proof).build()).queue());
    }

    public static CompletableFuture<Void> notifyMute(TextChannel tc, User u, long id, String mod, String modId, String reason, String proof, String duration) {
        tc.sendMessageEmbeds(mute(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, proof, duration).build()).queue();
        return u.openPrivateChannel().submit()
                .thenCompose(p -> p.sendMessageEmbeds(mute(u.getAvatarUrl(), id, u.getName(), u.getId(), mod, modId, reason, proof, duration).build()).submit())
                .handle((s, t) -> null);
    }

    private static EmbedBuilder ban(String avatar, long id, String uname, String uid, String mod, String modId, String reason, String appeal, String proof) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("**Banned " + uname + "**")
                .setDescription("**User id:**\n" + uid
                        + "\n**Moderator:**\n" + mod + " (" + modId + ")"
                        + "\n**Reason:**\n" + reason
                        + "\n**Appealable:**\n" + appeal)
                .setThumbnail(avatar)
                .setFooter("Case :" + id)
                .setColor(Color.red)
                .setTimestamp(OffsetDateTime.now());
        if (proof != null) em.setImage(proof);

        return em;
    }

    private static EmbedBuilder unban(String avatar, long id, String uname, String uid, String mod, String modId, String reason, String proof) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("**Unbanned " + uname + "**")
                .setDescription("**User id:**\n" + uid
                        + "\n**Moderator:**\n" + mod + " (" + modId + ")"
                        + "\n**Reason:**\n" + reason)
                .setThumbnail(avatar)
                .setFooter("Case :" + id)
                .setColor(Color.green)
                .setTimestamp(OffsetDateTime.now());
        if (proof != null) em.setImage(proof);
        return em;
    }

    private static EmbedBuilder supportBan(String avatar, long id, String uname, String uid, String mod, String modId, String reason, String appeal, String proof) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("**Support banned " + uname + "**")
                .setDescription("**User id:**\n" + uid
                        + "\n**Moderator:**\n" + mod + " (" + modId + ")"
                        + "\n**Reason:**\n" + reason
                        + "\n**Appealable:**\n" + appeal)
                .setThumbnail(avatar)
                .setFooter("Case :" + id)
                .setColor(Color.cyan)
                .setTimestamp(OffsetDateTime.now());
        if (proof != null) em.setImage(proof);
        return em;
    }

    private static EmbedBuilder mute(String avatar, long id, String uname, String uid, String mod, String modId, String reason, String proof, String duration) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("**Muted " + uname + "**")
                .setDescription("**User id:**\n" + uid
                        + "\n**Moderator:**\n" + mod + " (" + modId + ")"
                        + "\n**Duration:**\n" + duration
                        + "\n**Reason:**\n" + reason)
                .setThumbnail(avatar)
                .setFooter("Case :" + id)
                .setColor(Color.red)
                .setTimestamp(OffsetDateTime.now());
        if (proof != null) em.setImage(proof);
        return em;
    }

    private static EmbedBuilder kick(String avatar, long id, String uname, String uid, String mod, String modId, String reason, String proof) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("**Kicked " + uname + "**")
                .setDescription("**User id:**\n" + uid
                        + "\n**Moderator**:\n" + mod + " (" + modId + ")"
                        + "\n**Reason:**\n" + reason)
                .setThumbnail(avatar)
                .setFooter("Case :" + id)
                .setColor(Color.red)
                .setTimestamp(OffsetDateTime.now());
        if (proof != null) em.setImage(proof);
        return em;
    }

    private static EmbedBuilder warn(String avatar, long id, String uname, String uid, String mod, String modId, String reason, String proof) {
        EmbedBuilder em = new EmbedBuilder();
        em.setTitle("**Warned " + uname + "**")
                .setDescription("**User id:**\n" + uid
                        + "\n**Moderator**:\n" + mod + " (" + modId + ")"
                        + "\n**Reason:**\n" + reason)
                .setThumbnail(avatar)
                .setFooter("Case :" + id)
                .setColor(Color.gray)
                .setTimestamp(OffsetDateTime.now());
        if (proof != null) em.setImage(proof);
        return em;
    }
}
