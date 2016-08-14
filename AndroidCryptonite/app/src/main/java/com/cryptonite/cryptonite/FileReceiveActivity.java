package com.cryptonite.cryptonite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FileReceiveActivity extends AppCompatActivity {

    private int OTPnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_receive);

        Button receiveButton = (Button) findViewById(R.id.receive);

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        EditText otp = (EditText) findViewById(R.id.OTP);
        OTPnum = Integer.valueOf(otp.getText().toString());



    }


}
