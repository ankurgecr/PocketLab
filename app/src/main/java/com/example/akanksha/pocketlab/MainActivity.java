package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import android.util.Log;
import java.util.concurrent.ExecutionException;
import java.security.SecureRandom;


public class MainActivity extends ActionBarActivity {

    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    public static String currentUser = "";
    public static long exptime = 0; //gets from the chosen experiment

    Activity mSelf = this;
    TextView resultArea;
    Button loginButton;
    Button makeUserButton;
    EditText loginline;
    EditText passline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultArea = (TextView) findViewById(R.id.text_view);
        loginButton = (Button) findViewById(R.id.next_activity_button);
        makeUserButton = (Button) findViewById(R.id.make_new_user_button);
        loginline = (EditText) findViewById(R.id.enter_username);
        passline = (EditText) findViewById(R.id.enter_password);
        resultArea.setText("");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mSelf, HomeScreen.class);
                startActivity(intent);*/

                LoginSQL s = new LoginSQL(resultArea);
                String loginname = loginline.getText().toString().trim();
                String password = passline.getText().toString().trim();
                String value[] = new String[2];

                if (loginname.equals("")) {
                    return;
                }
                if (password.equals("")) {
                    return;
                }

                s.execute("SELECT * from login_info_pl WHERE loginname=\'" + loginname + "\'");

                try {
                    value = s.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (value[0].equals("")){
                    resultArea.setText("Username does not exist");
                    return;
                }

                password += value[0];

                //hash password
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA-256");
                }
                catch(NoSuchAlgorithmException e){
                    e.printStackTrace();
                }

                try{
                    md.update(password.getBytes("UTF-16")); // Change this to "UTF-16" if needed
                }
                catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                byte[] digest = md.digest();
                String hashedpass = String.format("%064x", new java.math.BigInteger(1, digest));

                if (value[1].equals("")) {
                    resultArea.setText("Username does not exist");
                } else if (!value[1].equals(hashedpass)) {
                    resultArea.setText("Incorrect Password");
                } else {
                    resultArea.setText("Login Successful");
                    currentUser = loginname;
                    Intent intent = new Intent(mSelf, HomeScreen.class);
                    startActivity(intent);
                }
            }

        });

        makeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //if they click on the Login button
                //check that login isn't void
                //verify that login is in database
                NewUserSQL s = new NewUserSQL(resultArea);
                String loginname = loginline.getText().toString();
                String password = passline.getText().toString();
                String value = "";
                if (loginname.equals("") || password.equals(""))
                {
                    resultArea.setText("Please enter a username and a password.");
                    return;
                }
                if(loginname.equals("None"))
                {
                    resultArea.setText("\'None\' is not a valid username.");
                    return;
                }
                if(loginname.length() > 20)
                {
                    resultArea.setText("Username may not be more than 20 characters long.");
                    return;
                }
                if(password.length() > 20)
                {
                    resultArea.setText("Password may not be more than 20 characters long.");
                    return;
                }
                //resultArea.setText(loginname);

                //generate salt and add to password
                SecureRandom random = null;
                try {
                    random = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                int saltvalue = random.nextInt();
                String saltstr = Integer.toString(saltvalue);
                password += saltstr;

                //hash password
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA-256");
                }
                catch(NoSuchAlgorithmException e){
                    e.printStackTrace();
                }

                try{
                    md.update(password.getBytes("UTF-16")); // Change this to "UTF-16" if needed
                }
                catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                byte[] digest = md.digest();
                String hashedpass = String.format("%064x", new java.math.BigInteger(1, digest));

                s.execute("INSERT INTO login_info_pl VALUES (\'"+loginname+"\',\'"+saltstr+"\',\'"+hashedpass+"\')",loginname);

                try
                {
                    value = s.get();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

                if(value.equals("exists"))
                {
                    resultArea.setText("Username already exists");
                }
                else if (value.equals("failed to add"))
                {
                    resultArea.setText("Re-try");
                }
                else
                {
                    resultArea.setText("Account created!");
                    currentUser = loginname;
                    Intent intent = new Intent(mSelf, HomeScreen.class);
                    startActivity(intent);
                };
                /*Intent intent = new Intent(mSelf, SecondActivity.class);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
