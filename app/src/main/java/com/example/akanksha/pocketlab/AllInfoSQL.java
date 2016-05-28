package com.example.akanksha.pocketlab;

/**
 * Created by melissaregalia on 5/25/16.
 */

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Created by melissaregalia on 5/25/16.
 */
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.lang.Long;

public class AllInfoSQL extends AsyncTask<String, Void, ArrayList<String>> {
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";
    Long timestamp;

    public AllInfoSQL(long time) {
        timestamp = time;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> tempretval = new ArrayList<String>();
        try {
            Class.forName(POSTGRESS_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tempretval.add(e.toString());
        }
        String url = "jdbc:postgresql://ec2-174-129-26-115.compute-1.amazonaws.com:5432/d4hp0ep351mjmr?user=mlcqisdxxxgoct&password=D1Cu5DZU0oi9Vy1L5QjY3WsbHU&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        Connection conn;

        try {
            DriverManager.setLoginTimeout(5);
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String sql, user;
            user = params[0]; //string for getting users
            sql = "SELECT * from expdata WHERE username =\'" + user + "\' AND timestamp =" + timestamp;
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++ ) {
                    String name = rsmd.getColumnName(i);
                    String tempstr = rs.getString(i).trim();
                    // Do stuff with name
                    if (!(name.equals("username") || name.equals("timestamp"))){
                        tempretval.add(name);
                        tempretval.add(tempstr);
                    }
                }
            }
            rs.close();

            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            tempretval.add(e.toString());
        }

        return tempretval;
    }

    @Override
    protected void onPostExecute(ArrayList<String> values) {

    }
}

