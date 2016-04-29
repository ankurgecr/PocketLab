package com.example.akanksha.pocketlab;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by melissaregalia on 4/20/16.
 */
public class NewExperimentSQL extends AsyncTask<String, Void, String> {
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    TextView resultArea;
    String mQuery;
    String loginname;

    //here is the intialization arguments - pass in a textview to initialize it
    //make this an error slot
    public NewExperimentSQL() {
    }

    @Override
    protected String doInBackground(String... params) {
        String retval = "";
        try {
            Class.forName(POSTGRESS_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            retval = e.toString();
        }
        String url = "jdbc:postgresql://ec2-174-129-26-115.compute-1.amazonaws.com:5432/d4hp0ep351mjmr?user=mlcqisdxxxgoct&password=D1Cu5DZU0oi9Vy1L5QjY3WsbHU&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        Connection conn;
        try {
            DriverManager.setLoginTimeout(5);
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String user, expname,recievedstring = "", sql, sqlcheck;
            user = params[0]; //string for inserting into table
            expname = params[1];
            long time = System.currentTimeMillis();

            sql = "INSERT INTO expdata VALUES (\'"+user+"\',"+time+",\'"+expname+"\',\'\',\'\',\'\',\'\')";
            sqlcheck = "SELECT * from expdata WHERE username =\'" + user + "\' AND timestamp =" + time;
            Log.d("DEBUG", sql);            //add the new experiment
            Log.d("DEBUG", sqlcheck);
            st.executeUpdate(sql);
            //now check and verify that it was added
            ResultSet rs = st.executeQuery(sqlcheck);
            while (rs.next()){
                recievedstring = (rs.getString("expname")).trim();
            }
            //if failed to add
            if(!recievedstring.equals(expname)){
                st.close();
                conn.close();
                return "failed to add";
            }
            //if added correctly
            MainActivity.exptime = time;
            retval = "Works";
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            retval = e.toString();
        }
        return retval;
    }
    @Override
    protected void onPostExecute(String value) {
        //resultArea.setText(value);
    }
}
