package com.example.akanksha.pocketlab;

/*
 * Created by melissaregalia on 3/8/16.
 */

import android.os.AsyncTask;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewUserSQL extends AsyncTask<String, Void, String> {
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    TextView resultArea;
    String mQuery;
    String loginname;

    //here is the intialization arguments - pass in a textview to initialize it
    //make this an error slot
    public NewUserSQL(TextView textView) {
        this.resultArea = textView; //error box
        //this.mQuery = myString; //password user entered
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
            String sql, sqllogin, logincompare;
            logincompare = "";
            loginname = params[1];
            sql = params[0]; //string for inserting into table
            sqllogin = "SELECT * FROM login_info_pl WHERE loginname=\'"+loginname+"\'";

            ResultSet rs = st.executeQuery(sqllogin);
            while(rs.next()) {
                //FOR SOME REASON THIS ADDS SOME WHITESPACE
                logincompare = (rs.getString("password")).trim();
            }
            rs.close();
            //need to compare here, if it is empty that means it doesn't exist which is what we want
            if (!logincompare.equals("")){
                st.close();
                conn.close();
                return "exists";
            }
            //add the new login
            st.executeUpdate(sql);
            //now check and verify that it was added
            rs = st.executeQuery(sqllogin);
            while (rs.next()){
                logincompare = (rs.getString("loginname")).trim();
            }
            //if failed to add
            if(logincompare.equals("")){
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
