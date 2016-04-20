package com.example.akanksha.pocketlab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;


public class HomeScreen extends ActionBarActivity {
    Button newExpButton;
    Activity mSelf = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        newExpButton = (Button) findViewById(R.id.new_experiment_button);

        /*newExpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeScreenSelf, NewExperiment.class);
                startActivity(intent);
            }
        });*/
        newExpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // from http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(mSelf);

                //alert.setTitle("Title");
                alert.setTitle("Enter a name for your new experiment");
                alert.setMessage("");

                // Set an EditText view to get user input
                final EditText input = new EditText(mSelf);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                // from http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String exp = input.getText().toString();
                        boolean success = false;

                        if(exp.length() >= 40) {
                            dialog.setMessage("Too long, must not exceed 40 characters");
                        }
                        if (exp.equals("")) {
                            dialog.setMessage("Experiment must have a name");
                        }
                        else{
                            NewExperimentSQL s = new NewExperimentSQL();
                            s.execute(MainActivity.currentUser, exp);
                            String result = "Works";//s.get();

                            if (result.equals("Works")) {
                                dialog.dismiss();
                                AlertDialog.Builder dialog2 = new AlertDialog.Builder(mSelf);
                                //alert.setTitle("Title");
                                dialog2.setTitle("Made new experiment \"" + exp + "\"!");

                                dialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent intent = new Intent(mSelf, NewExperiment.class);
                                        startActivity(intent);
                                    }
                                });

                                dialog2.show();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
