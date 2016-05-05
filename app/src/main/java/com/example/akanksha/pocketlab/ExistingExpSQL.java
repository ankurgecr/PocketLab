package com.example.akanksha.pocketlab;

/**
 * Created by melissaregalia on 5/4/16.
 */
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.lang.Long;

public class ExistingExpSQL extends AsyncTask<String, Void, ArrayList<String>> {
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    public ExistingExpSQL() {

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
            String sql;
            sql = params[0]; //string for getting users
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String tempstr = rs.getString("expname").trim();
                tempretval.add(tempstr);
                long templong = rs.getLong("timestamp");
                tempstr = Long.toString(templong);
                tempretval.add(tempstr);
            }
            rs.close();

            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            tempretval.add(e.toString());
        }
        //CharSequence[] retval = tempretval.toArray(new CharSequence[tempretval.size()]);
        return tempretval;
    }

    @Override
    protected void onPostExecute(ArrayList<String> values) {

    }
}

