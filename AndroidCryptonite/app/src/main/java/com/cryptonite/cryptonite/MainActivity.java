package com.cryptonite.cryptonite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Function.Client_Logout;
import Function.Client_Server_Connector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button filesend = (Button) findViewById(R.id.file_send_button);
        Button filerecevice = (Button) findViewById(R.id.file_receive_button);

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        new Client_Logout();
        finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
