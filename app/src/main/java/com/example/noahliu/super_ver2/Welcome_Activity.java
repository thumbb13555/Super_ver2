package com.example.noahliu.super_ver2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome_Activity extends AppCompatActivity {
    Button btNew,btLog;

    public static Activity closefromlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_main);

        btNew = (Button) findViewById(R.id.btNewMember);
        btLog = (Button) findViewById(R.id.btLogin);
        closefromlogin=this;

        btNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goNew = new Intent(Welcome_Activity.this,MemberCreate_Activity.class);
                startActivity(goNew);
            }
        });//btNew
        btLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin = new Intent(Welcome_Activity.this,Login_Activity.class);
                startActivity(goLogin);
            }
        });

    }//onCreate


}
