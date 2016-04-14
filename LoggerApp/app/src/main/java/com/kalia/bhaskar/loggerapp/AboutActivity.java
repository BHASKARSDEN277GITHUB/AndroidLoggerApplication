package com.kalia.bhaskar.loggerapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tv = (TextView)findViewById(R.id.aboutText);
        String aboutDetails = "Developer : "+"Bhaskar Kalia";
        aboutDetails = aboutDetails + System.getProperty("line.separator");
        aboutDetails = aboutDetails + "Version : 1.0";
        aboutDetails = aboutDetails + System.getProperty("line.separator");
        aboutDetails = aboutDetails + "Details : "+System.getProperty("line.separator")+"This application logs Running Applications/Services on Screen Lock and Unlock Events.";
        tv.setText(aboutDetails);
    }

}
