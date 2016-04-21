package com.example.akanksha.pocketlab;

/**
 * Created by melissaregalia on 4/20/16.
 */

import android.os.AsyncTask;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by melissaregalia on 4/20/16.
 */
public class SaveDataPointSQL extends AsyncTask<String, Void, String> {
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    TextView resultArea;
    String mQuery;
    String loginname;
    Long timestamp;

    //here is the intialization arguments - pass in a textview to initialize it
    //make this an error slot
    public SaveDataPointSQL(long time) {
        timestamp = time;
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
            String user, expname,recievedstring = "", sql, sqlcheck, columnname, newval = "", datastring;
            user = params[0]; //string for inserting into table
            expname = params[1];
            columnname = params[2];
            datastring = params[3];

            //gets the existing value for the column, appends a data point, updates the column, and then verifies update

            sqlcheck = "SELECT * from expdata WHERE user =\'" + user + "\' AND timestamp =" + timestamp;

            //get existing value
            ResultSet rs = st.executeQuery(sqlcheck);

            while (rs.next()){
                newval = (rs.getString(columnname)).trim();
            }
            //append new data point
            newval += ","+datastring;

            //save new value
            sql = "UPDATE expdata SET " + columnname + "=" + newval + " WHERE user =\'" + user + "\' AND timestamp =" + timestamp;
            st.executeUpdate(sql);

            //check to see if added
            rs = st.executeQuery(sqlcheck);
            while (rs.next()){
                recievedstring = (rs.getString(columnname)).trim();
            }
            //if failed to add
            if(!recievedstring.equals(newval)){
                st.close();
                conn.close();
                return "failed to add";
            }
            //if added correctly
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
