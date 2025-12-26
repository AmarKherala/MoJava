package net.amar.mojava.db;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import net.amar.mojava.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DBCaseFinder {
    public static EmbedBuilder findCaseEmbed(long caseId) {
        String sql = "SELECT * FROM cases WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DBManager.sqlDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, caseId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new EmbedBuilder()
                    .setColor(Color.cyan)
                    .setTitle("Case #" + rs.getLong("id"))
                    .addField("User", rs.getString("uname") + " (" + rs.getString("uid") + ")", false)
                    .addField("Moderator", rs.getString("mod_name"), true)
                    .addField("Action", rs.getString("action"), true)
                    .addField("Reason", rs.getString("reason"), false)
                    .addField("Duration", rs.getString("duration"), true)
                    .addField("Appeal", rs.getString("appeal"), true)
                    .setFooter("Created at " + rs.getString("timestamp"));

        } catch (SQLException e) {
            Log.error("failed to build case embed", e);
            return null;
        }
    }

    public static void findUserCases(User target, User req, TextChannel tc, EventWaiter waiter) {
        List<EmbedBuilder> pages = new ArrayList<>();
        String sqlStmt = """
                SELECT id, action, reason, mod_name FROM cases
                WHERE uid = ?
                ORDER BY id ASC
                """;
        try (Connection conn = DriverManager.getConnection(DBManager.sqlDB);
             PreparedStatement ps = conn.prepareStatement(sqlStmt)) {

            ps.setString(1, target.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int caseId = rs.getInt("id");
                String action = rs.getString("action");
                String reason = rs.getString("reason");
                String mod = rs.getString("mod_name");

                pages.add(
                        new EmbedBuilder()
                                .setTitle("Cases for " + target.getName())
                                .setThumbnail(target.getAvatarUrl())
                                .setColor(Color.RED)
                                .addField("Case ID", String.valueOf(caseId), false)
                                .addField("Moderator", mod, false)
                                .addField("Action ", action, false)
                                .addField("Reason", reason, false)
                );
            }
            if (pages.isEmpty()) {
                tc.sendMessage("no cases found").queue();
                return;
            }

            EmbedPaginator paginator = new EmbedPaginator.Builder()
                    .setEventWaiter(waiter)
                    .wrapPageEnds(true)
                    .setUsers(req)
                    .waitOnSinglePage(false)
                    .addItems(pages.stream()
                            .map(EmbedBuilder::build)
                            .collect(Collectors.toList()))
                    .setFinalAction(m -> m.clearReactions().queue())
                    .setTimeout(1, TimeUnit.MINUTES)
                    .build();

            paginator.display(tc);

        } catch (SQLException e) {
            Log.error("smth went wrong trying to get cases for user",e);
            tc.sendMessageFormat("failed to get cases [%s]", e.getMessage()).queue();
        }
    }
}
