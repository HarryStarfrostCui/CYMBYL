package com.example.harry.cymbyl_3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harry.cymbyl_3.Objects.Notification_reciever;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class NotificationActivity extends AppCompatActivity {
    private static final String FILE_NAME = "script.txt";
    private ArrayList<Integer> feq;
    private String mMessage;
    TextView display;
    EditText input;
    Button enter, reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        feq = new ArrayList<Integer>();
        mMessage = "ERROR";
        display = findViewById(R.id.mantra_display);
        input = findViewById(R.id.mantra_input);
        enter = findViewById(R.id.enter);
        reset = findViewById(R.id.Edit);
        reset.setVisibility(View.INVISIBLE);
        load();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = input.getText().toString();
                if(temp.equals(mMessage)){
                    if(feq.get(0)>0) {
                        save(feq);
                    }
                    display.setText("Congrats! You've solved the puzzle! ");
                    enter.setVisibility(View.INVISIBLE);
                    reset.setVisibility(View.VISIBLE);
                }
                else{
                    input.setError("Uh-oh! Incorrect Mantra!");
                    input.requestFocus();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain();
            }
        });

        display.setText(shuffle(mMessage));
    }

    private void startMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void load(){
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
    }

    public String shuffle(String str){
        if(str == null){
            return "ERROR";
        }
        ArrayList<String> words = new ArrayList<>();
        while(str.indexOf(' ') != -1){
            int x = str.indexOf(' ');
            String word = str.substring(0, x+1);
            str = str.substring(x+1);
            words.add(word);
        }
        words.add(str+" ");
        str = "";
        Random rn = new Random();
        while(!words.isEmpty()){
            int x = Math.abs(rn.nextInt() % words.size());
            str = str + words.remove(x);
        }
        return str;
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"0",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static int[] getCurrentTime() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        int[] currentTime = {Integer.valueOf(formattedDate.substring(0,2)),
                Integer.valueOf(formattedDate.substring(3,5))};

        return currentTime;
    }

    public  ArrayList<Integer> changeNextTime(ArrayList<Integer> feq){
        int[] currentTime = getCurrentTime();
        int temp = feq.get(1)+currentTime[1];
        if(temp >60){
            temp = temp % 60;
            feq.set(6, (currentTime[0]+1)%24);
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
