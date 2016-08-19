package com.cryptonite.cryptonite;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.ArrayList;

import Function.C_Toast;
import Function.Client_FileShare_Send;
import Function.Client_Logout;
import Function.PathPicker;

import static Function.isApplicationSentToBackground.isApplicationSentToBackground;

public class FileSendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_send);

        Button choose = (Button) findViewById(R.id.send);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose();
            }
        });

    }

    private void fileChoose(){
        PathPicker picker = new PathPicker(this,PathPicker.Select_Files);
        picker.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(files.length!=0) {
                    fileSendTask fs = new fileSendTask();
                    fs.execute(files);
                } else {
                    new C_Toast(getApplicationContext()).showToast("File doesn't selected",Toast.LENGTH_LONG);
                }
            }
        });
        picker.show();
    }

    class fileSendTask extends AsyncTask<String,Integer,Boolean>
    {
        Client_FileShare_Send cfs;
        fileSendTask()
        {
            cfs = new Client_FileShare_Send();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("File",params[0]);
            cfs.sendFile(params);
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if(isApplicationSentToBackground(this)) {
            new Client_Logout();
            super.onDestroy();
        } else {
            super.onDestroy();
        }
    }

}


/*
https://android-arsenal.com/details/1/3950
 */