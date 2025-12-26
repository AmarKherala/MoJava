package net.amar.mojava.db;

import net.amar.mojava.Log;

import java.sql.*;

public class DBInsertCase {

    private static final String INSERT_SQL = """
        INSERT INTO cases
        (uname, uid, mod_name, mod_id, action, duration, reason, appeal)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static long insertCase(String uname, String uid, String mod, String modId, String action, String duration, String reason, Boolean appeal) {
        try (Connection conn = DriverManager.getConnection(DBManager.sqlDB);
             PreparedStatement ps =
                     conn.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, uname);
            ps.setString(2, uid);
            ps.setString(3, mod);
            ps.setString(4, modId);
            ps.setString(5, action);

            if (duration == null) {
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(6, duration);
            }

            ps.setString(7, reason);

            if (appeal == null) {
                ps.setNull(8, Types.INTEGER);
            } else {
                ps.setInt(8, appeal ? 1 : 0);
            }

            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long caseId = rs.getLong(1);
                    Log.info("Inserted new case with ID: "+caseId);
                    return caseId;
                }
            }

            throw new SQLException("Case inserted but ID was not returned");

        } catch (SQLException e) {
            Log.error("Failed to insert new case", e);
            return -1;
        }
    }

    public static long ban(String uname, String uid, String mod, String modId, String reason, Boolean appeal){
        return insertCase(uname, uid, mod, modId, "BAN", null,reason, appeal);
    }

    public static long unban(String uname, String uid, String mod, String modId, String reason) {
        return insertCase(uname, uid, mod, modId, "UNBAN", null, reason, null);
    }

    public static long supportBan(String uname, String uid, String mod, String modId, String reason, Boolean appeal) {
        return insertCase(uname, uid, mod, modId, "SUPPORT_BAN",null, reason, appeal);
    }

    public static long kick(String uname, String uid, String mod, String modId, String reason) {
        return insertCase(uname, uid, mod, modId, "KICK", null, reason, null);
    }

    public static long timeout(String uname, String uid, String mod, String modId, String reason, String duration) {
        return insertCase(uname, uid, mod, modId, "TIMEOUT", duration, reason, null);
    }

    public static long warn(String uname, String uid, String mod, String modId, String reason) {
        return insertCase(uname, uid, mod, modId, "WARN", null, reason, null);
    }
}
