package com.neto.studayapp.connection;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDatabase extends AsyncTask <Void, Void, Void> {

//    private final String DB_URL = "jdbc:postgresql://localhost:5432/studay?useTimezone=tue&serverTimezone=UTC";
//    private final String DB_USER = "default";
//    private final String DB_PASS = "secret";
//    private final String DRIVER = "org.postgresql.Driver";

    private final String DB_URL = "jdbc:postgresql://motty.db.elephantsql.com:5432/wvdipjdj?useTimezone=tue&serverTimezone=UTC";
    private final String DB_USER = "wvdipjdj";
    private final String DB_PASS = "PvUMBCf7of2Mog4Cuujv6z9L2mDtwA5M";
    private final String DRIVER = "org.postgresql.Driver";

    public ConnectionDatabase() {}

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.createStatement().executeQuery("SHOW TABLES");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void testConnect() {
        try {
            Connection conn = connect();
            System.out.println(conn);
            //conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
