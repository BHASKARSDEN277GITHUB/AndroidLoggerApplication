package services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackgroundScreenService extends Service {

    private static String TAG = "Implementing Logger Service";
    private static List<String> screenOnActiveList = new ArrayList<String>();
    private static List<String> screenOffActiveList = new ArrayList<String>();
    private final String fileName = "loggerAppLogs.txt";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Register service that handles screen off and on logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if(intent != null && intent.getComponent().getClassName().equals("services.BackgroundScreenService")){
            boolean screenOn = intent.getBooleanExtra("screen_state", false);
            String screenStatus = !screenOn ? "ON" : "OFF";

            Log.d(TAG,"BackgroundScreenService Screen Status: "+screenStatus);

            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
            List<String> currentApps = new ArrayList<String>();
            List<String> currenSessionApps = new ArrayList<String>();

            //get current apps
            for (int i = 0; i < list.size(); i++) {
                int length = list.get(i).processName.split("\\.").length;
                String appName = length > 0 ? list.get(i).processName.split("\\.")[length-1] : list.get(i).processName;
                currentApps.add(appName);
                //String fullName = list.get(i).processName;
                /*String[] name=fullName.split("\\.");*/
                //Log.d(TAG,appName);
                //Log.d(TAG, ""+name.toString());

            }


            BufferedWriter writer = null;
            if(!screenOn){
                // Screen is off
                Log.d(TAG,"off");

                for(int i=0;i<currentApps.size();i++){
                    screenOffActiveList.add(i,currentApps.get(i));
                    Log.d(TAG,currentApps.get(i));
                }

            /*    //get difference
            if(screenOnActiveList.size() > 0 && screenOffActiveList.size() >0){
                int i = screenOnActiveList.size() , j = screenOffActiveList.size();
                int max,min;
                int k = 0;
                max = i > j ? i : j;
                min = i < j ? i : j;
                boolean flagInclude = true; // true to include
                boolean flagShortGone = false; // false for both loops running
                String current = "";

                for(i=0;i<max;i++){
                    for(j=0;j<min;j++){
                        // if equal set to flag
                        if(screenOnActiveList.size() >= screenOffActiveList.size()){ //screenOn is greater
                            if(screenOnActiveList.get(i).equals(screenOffActiveList.get(j))){
                                flagInclude = false;
                            }else{
                                current = screenOnActiveList.get(i);
                            }
                        }else {
                            if (screenOffActiveList.get(i).equals(screenOnActiveList.get(j))) {
                                flagInclude = false;
                            } else {
                                current = screenOffActiveList.get(i);
                            }
                        }
                    }
                    if(j==min){
                        flagShortGone = true;
                    }
                    if(flagInclude){
                        if(flagShortGone){
                            //include all with i
                            if(screenOnActiveList.size() >= screenOffActiveList.size()){
                                currenSessionApps.add(k,screenOnActiveList.get(i));
                            }else{
                                currenSessionApps.add(k, screenOffActiveList.get(i));
                            }
                            k++;
                        }else{
                            //include current
                            currenSessionApps.add(k,current);
                            k++;
                        }
                    }
                    flagInclude = true;
                }
            }else{
                // let go
            }*/

                try{
                    FileOutputStream outputstream = openFileOutput("loggerAppLogs.txt", Context.MODE_APPEND);
                    writer = new BufferedWriter(new OutputStreamWriter(outputstream));
                    String message = new Date().toString()+" : Screen Turned OFF ";
                    String prevApps = screenOnActiveList.toString();
                    String newApps = screenOffActiveList.toString();

                    writer.newLine();
                    writer.newLine();
                    writer.write(message);
                    writer.newLine();
                    writer.newLine();

                    writer.write("Apps status For this Event :");
                    writer.newLine();
                    writer.newLine();
                    for(int l=0;l<currentApps.size();l++){
                        writer.write(currentApps.get(l));
                        writer.newLine();
                    }
                    writer.newLine();
                    writer.newLine();
                    writer.close();
                    /*Toast.makeText(getApplicationContext(),"Data written to file : \n" + data,Toast.LENGTH_LONG).show();*/
                    outputstream.close();
                }catch(Exception e){

                }
            }else {
                // Screen is on

                Log.d(TAG,"on");
                for(int i=0;i<currentApps.size();i++){
                    screenOnActiveList.add(i,currentApps.get(i));
                    Log.d(TAG,currentApps.get(i));
                }

                try{
                    FileOutputStream outputstream = openFileOutput("loggerAppLogs.txt", Context.MODE_APPEND);
                    writer = new BufferedWriter(new OutputStreamWriter(outputstream));
                    String data = new Date().toString()+" : Screen Turned ON";
                    writer.newLine();
                    writer.newLine();
                    writer.write(data);
                    writer.newLine();
                    writer.newLine();
                    writer.write("Apps status For this Event :");
                    writer.newLine();
                    writer.newLine();
                    for(int l=0;l<currentApps.size();l++){
                        writer.write(currentApps.get(l));
                        writer.newLine();
                    }
                    writer.newLine();
                    writer.newLine();
                    writer.close();
                    /*Toast.makeText(getApplicationContext(),"Data written to file : \n" + data,Toast.LENGTH_LONG).show();*/
                    outputstream.close();
                }catch(Exception e){

                }
            }
        }else{
            Log.d(TAG,"intent is null");
        }



        //create a log file if it does not exist
        /*String toWrite = "File Created on : "+ new Date().toString();
        try
        {
            File loggerApp = new File(Environment.getExternalStorageDirectory(),"LoggerApp");

            if (!loggerApp.exists())
            {
                loggerApp.mkdirs();
            }

            File logFile = new File(loggerApp, fileName);
            FileWriter writer = new FileWriter(logFile,true);
            writer.append(toWrite+"\n\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "BackgroundScreenService: Data has been written to LoggerApp/"+fileName, Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }*/
    }

    @Override
    public void onDestroy() {
        startService(new Intent(this, BackgroundScreenService.class));
        Log.d(TAG,"Restarted Service");
    }

}
