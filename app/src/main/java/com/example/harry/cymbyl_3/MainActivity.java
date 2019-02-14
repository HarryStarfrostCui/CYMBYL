package com.example.harry.cymbyl_3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.example.harry.cymbyl_3.Fragments.AboutFragment;
import com.example.harry.cymbyl_3.Fragments.DescriptionFragment;
import com.example.harry.cymbyl_3.Fragments.FrequencyFragment;
import com.example.harry.cymbyl_3.Fragments.InputFragment;
import com.example.harry.cymbyl_3.Fragments.StatusFragment;
import com.example.harry.cymbyl_3.Objects.Notification_reciever;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "script.txt";
    private String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessage = "";
        load();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AboutFragment()).addToBackStack(null).commit();
    }

    public void setMessage(String message){
        mMessage = message;
    }

    public ArrayList<Integer> load(){
        ArrayList<Integer> feq = new ArrayList<>();
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String txt;
            int count = 0;
            while((txt = br.readLine())!=null && count < 8){
                feq.add(Integer.valueOf(txt));
                count++;
            }
            mMessage = txt;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "reading error", Toast.LENGTH_SHORT).show();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return feq;
    }

    public void save(ArrayList<Integer> feq) {
        feq = changeNextTime(feq);
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            for(int i = 0; i<8; i++){
                fos.write((Integer.toString(feq.get(i))+"\n").getBytes());
            }
            fos.write(mMessage.getBytes());
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"0",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
        }

    }

    public void changeFragment(int x){
        switch (x){
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DescriptionFragment()).addToBackStack(null).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).addToBackStack(null).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InputFragment()).addToBackStack(null).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FrequencyFragment()).addToBackStack(null).commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatusFragment()).addToBackStack(null).commit();
                break;
        }
    }

    public  ArrayList<Integer> changeNextTime(ArrayList<Integer> feq){
        int temp = feq.get(1)+feq.get(7);
        if(temp >60){
            temp = temp % 60;
            feq.set(6, (feq.get(6)+1)%24);
        }
        feq.set(7,temp);
        if(feq.get(6)>feq.get(4) && feq.get(7)>feq.get(5)){
            feq.set(6, feq.get(2));
            feq.set(7, feq.get(3));
        }
        feq.set(0,feq.get(0)-1);

        Calendar nextAlarm = Calendar.getInstance();

        nextAlarm.set(Calendar.HOUR_OF_DAY, feq.get(6));
        nextAlarm.set(Calendar.MINUTE, feq.get(7));

        Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);

        PendingIntent PI = PendingIntent.getBroadcast(getApplicationContext(),100,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, nextAlarm.getTimeInMillis(), PI);

        return feq;
    }
}
