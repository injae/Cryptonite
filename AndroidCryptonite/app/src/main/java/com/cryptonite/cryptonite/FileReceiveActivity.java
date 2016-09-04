package com.cryptonite.cryptonite;

import android.media.Image;
import android.os.AsyncTask;
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

import Function.C_Toast;
import Function.Client_FileShare_Receive;
import Function.Client_FileShare_Send;
import Function.Client_Logout;
import Function.PathPicker;

import static Function.isApplicationSentToBackground.isApplicationSentToBackground;

public class FileReceiveActivity extends AppCompatActivity {

    private TextView path,downloading;
    private EditText otp;
    private ImageButton receiveButton;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_receive);

        receiveButton = (ImageButton) findViewById(R.id.receive);
        progressBar = (ProgressBar) findViewById(R.id.Download_progressBar);
        downloading = (TextView) findViewById(R.id.Downloading_TextView);

        ImageButton selectButton = (ImageButton) findViewById(R.id.Path_Select_Button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dirChoose();
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveButton.setVisibility(View.GONE);
                downloading.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                new Client_FileShare_Receive(getApplicationContext(), downloading, progressBar,otp,receiveButton).execute(path.getText().toString(),otp.getText().toString());
            }
        });

        otp = (EditText) findViewById(R.id.OTP);
        path = (TextView) findViewById(R.id.Path_View);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Client_FileShare_Receive.isReceiving == true) {
            receiveButton.setVisibility(View.GONE);
            downloading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Client_FileShare_Receive.init(downloading, progressBar, otp, receiveButton);
            downloading.setText(Client_FileShare_Receive._fileName);
        }
    }

    private void dirChoose()
    {
        PathPicker p = new PathPicker(this,PathPicker.Select_Dir);
        p.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(files.length!=0) {
                    path.setText(files[0]);
                    path.setVisibility(View.VISIBLE);
                    receiveButton.setVisibility(View.VISIBLE);
                }
                else {
                    new C_Toast(getApplicationContext()).showToast("Dir doesn't selected", Toast.LENGTH_LONG);
                }
            }
        });
        p.show();
    }






}
