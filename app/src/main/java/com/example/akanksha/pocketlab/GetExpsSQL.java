package com.example.akanksha.pocketlab;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melissaregalia on 4/20/16.
 */
public class GetExpsSQL extends AsyncTask<String, Void, List<String>>
{
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    public GetExpsSQL ()
    {

    }

    @Override
    protected List<String> doInBackground(String... params)
    {
        List<String> retval = new ArrayList<String>();
        try
        {
            Class.forName(POSTGRESS_DRIVER);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            retval.add(e.toString());
        }
        String url = "jdbc:postgresql://ec2-174-129-26-115.compute-1.amazonaws.com:5432/d4hp0ep351mjmr?user=mlcqisdxxxgoct&password=D1Cu5DZU0oi9Vy1L5QjY3WsbHU&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        Connection conn;

        try
        {
            DriverManager.setLoginTimeout(5);
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String sql;
            sql = params[0]; //string for getting users
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                retval.add((rs.getString(1)).trim());
            }
            rs.close();

            st.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
            retval.add(e.toString());
        }
        return retval;
    }
    @Override
    protected void onPostExecute(List<String> values)
    {

    }
}
