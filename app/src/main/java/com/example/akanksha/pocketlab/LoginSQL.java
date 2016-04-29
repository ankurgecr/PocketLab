package com.example.akanksha.pocketlab;

/**
 * Created by melissaregalia on 2/24/16.
 */

import android.os.AsyncTask;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginSQL extends AsyncTask<String, Void, String[]> {

    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    TextView resultArea;
    String mQuery;
    String password;
    //public int success;// = false;

    //here is the intialization arguments - pass in a textview to initialize it
    //make this an error slot
    public LoginSQL(TextView textView) {
        //success = 0;
        this.resultArea = textView; //error box
        //this.mQuery = myString; //password user entered
    }

    @Override
    protected String[] doInBackground(String... params) {
        String retval[] = new String[2];
        retval[0] = "";
        retval[1] = "";
        try {
            Class.forName(POSTGRESS_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            retval[0] = e.toString();
        }
        String url = "jdbc:postgresql://ec2-174-129-26-115.compute-1.amazonaws.com:5432/d4hp0ep351mjmr?user=mlcqisdxxxgoct&password=D1Cu5DZU0oi9Vy1L5QjY3WsbHU&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        Connection conn;
        try {
            DriverManager.setLoginTimeout(5);
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String sql;
            sql = params[0];//"SELECT * from \"dummyTable\" WHERE id=1";
            //password = params[1];
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                //FOR SOME REASON THIS ADDS SOME WHITESPACE
                retval[0] = (rs.getString("salt")).trim();
                retval[1] = (rs.getString("password")).trim();

            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            retval[0] = e.toString();
        }
        return retval;
    }
    @Override
    protected void onPostExecute(String[] value) {
        //if there is no such id then it is empty
        /*
        //Log.d(LoginSQL.class.getSimpleName(), "got to post execute");
        if(value == "" ){
            success = -1;
            //Log.d(LoginSQL.class.getSimpleName(), "just changed success to " + success);
            resultArea.setText("No such id");
        }
        else if (!value.equals(password)){ //
            success = -1;
            //Log.d(LoginSQL.class.getSimpleName(), "just changed success to " + success);
            resultArea.setText("Incorrect Password");
        }
        else {
            //Log.d(LoginSQL.class.getSimpleName(), "login is successful");
            success = 1;
            //Log.d(LoginSQL.class.getSimpleName(), "just changed success to " + success);
            resultArea.setText("WORKS");
            //Log.d(LoginSQL.class.getSimpleName(), "success " + success);
        };
        */
    }
}
