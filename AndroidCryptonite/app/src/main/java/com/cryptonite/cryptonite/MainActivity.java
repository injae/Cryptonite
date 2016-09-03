package com.cryptonite.cryptonite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import Function.Client_Logout;
import Function.Client_Server_Connector;

import static Function.isApplicationSentToBackground.isApplicationSentToBackground;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton filesend = (ImageButton) findViewById(R.id.file_send_button);
        ImageButton filerecevice = (ImageButton) findViewById(R.id.file_receive_button);
        ImageButton makegroup = (ImageButton) findViewById(R.id.make_group_button);
        ImageButton grouplist = (ImageButton) findViewById(R.id.group_list_button);

        filesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FileSendActivity.class));
            }
        });

        filerecevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FileReceiveActivity.class));
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MakeGroupActivity.class));
            }
        });

        grouplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),GroupListActivity.class));
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isApplicationSentToBackground(this)) {
            new Client_Logout();
            finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
