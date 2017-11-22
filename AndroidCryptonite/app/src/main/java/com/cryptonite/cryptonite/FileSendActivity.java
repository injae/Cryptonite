package com.cryptonite.cryptonite;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import Function.C_Toast;
import Function.Client_FileShare_Send;
import Function.Client_Logout;
import Function.PathPicker;

import static Function.isApplicationSentToBackground.isApplicationSentToBackground;

public class FileSendActivity extends AppCompatActivity {

    ImageButton choose;
    ProgressBar progressBar;
    TextView step2,step3,OTP;
    EditText receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_send);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        step2 = (TextView) findViewById(R.id.Step2_TextView);
        step3 = (TextView) findViewById(R.id.Step3_TextView);
        OTP = (TextView) findViewById(R.id.OTP_View);
        receiver = (EditText) findViewById(R.id.receiver);


        choose = (ImageButton) findViewById(R.id.send);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Client_FileShare_Send.isSending == true)
        {
            choose.setClickable(false);
            step2.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Client_FileShare_Send.init(progressBar,step2,step3,OTP,receiver.getText().toString());
        }



    }

    private void fileChoose(){
        if (receiver.getText().toString().isEmpty())
        {
            new C_Toast(getApplicationContext()).showToast("Please Input Receiver ID",Toast.LENGTH_LONG);
            return;
        }

        PathPicker picker = new PathPicker(this,PathPicker.Select_Files);
        picker.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(files.length!=0) {

                    new Client_FileShare_Send(progressBar,step2, step3,OTP,receiver.getText().toString()).execute(files); // send Files

                    choose.setClickable(false);
                    step2.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                } else {
                    new C_Toast(getApplicationContext()).showToast("File doesn't selected",Toast.LENGTH_LONG);
                }
            }
        });
        picker.show();
    }

}


/*
https://android-arsenal.com/details/1/3950
 */