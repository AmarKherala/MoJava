package net.amar.mojava.db;

import net.amar.mojava.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    public static final String sqlDB = "jdbc:sqlite:cases.db";

    public static void initDB(){
        try (Connection conn = DriverManager.getConnection(sqlDB);
             Statement stmt = conn.createStatement()) {

            String tableStmt = """
                    CREATE TABLE IF NOT EXISTS cases (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    uname TEXT NOT NULL,
                    uid TEXT NOT NULL,
                    mod_name TEXT NOT NULL,
                    mod_id TEXT NOT NULL,
                    action TEXT NOT NULL,
                    duration TEXT,
                    appeal INTEGER,
                    reason TEXT NOT NULL,
                    timestamp TEXT DEFAULT CURRENT_TIMESTAMP
                    );
                    """;

            stmt.execute(tableStmt);
            Log.info("DB initialized at "+sqlDB);

        } catch (SQLException e) {
            Log.error("skill issue detected",e);
        }
    }
}
