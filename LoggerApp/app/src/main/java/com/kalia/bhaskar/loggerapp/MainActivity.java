package com.kalia.bhaskar.loggerapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

import services.BackgroundScreenService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button updateLogs = (Button)findViewById(R.id.updateButton);
        Button deleteLogs = (Button)findViewById(R.id.deleteButton);

        // Read logs
        readWriteLogs();

        // Start the background service
        startService(new Intent(this, BackgroundScreenService.class));


        updateLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                readWriteLogs();
                Toast.makeText(getApplicationContext(),"Logs Updated",Toast.LENGTH_SHORT).show();
            }
        });

        deleteLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                File dir = getFilesDir();
                File file = new File(dir,"loggerAppLogs.txt");
                boolean deleted = file.delete();
                if(deleted){
                    Toast.makeText(getApplicationContext(),"Logs Deleted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Unable to delete logs",Toast.LENGTH_SHORT).show();
                }

                readWriteLogs();
            }
        });


    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                about();
                return true;
            default:
                return true ;
        }
    }

    // New behaviour methods here
    private void readWriteLogs(){
        TextView textView = (TextView)findViewById(R.id.tv1);
        textView.setText("Logger Applicaton Bootstrapped");

        // Read logs from file if it exists/logs are recorded already
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String logs = "";
        try{
            FileInputStream inputstream = openFileInput("loggerAppLogs.txt");
            if(inputstream != null){
                reader = new BufferedReader(new InputStreamReader(inputstream));
                String str = reader.readLine();
                while(str != null){
                    logs = logs + str +"\n";
                    str = reader.readLine();
                }
                textView.setText("Logger Applicaton Bootstrapped \n"+logs);
                reader.close();
            }else{
                Log.d("Logs : Main Activity : ","File does not exist");
                // Create file instance
                FileOutputStream outputstream = openFileOutput("loggerAppLogs.txt", Context.MODE_PRIVATE);
                writer = new BufferedWriter(new OutputStreamWriter(outputstream));
                String data = "LoggerApp Started";
                writer.write(data);
                writer.close();
                Toast.makeText(getApplicationContext(),"Data written to file : \n" + data,Toast.LENGTH_LONG).show();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void about(){
        Intent i = new Intent(getApplicationContext(),AboutActivity.class);
        startActivity(i);
    }

}
