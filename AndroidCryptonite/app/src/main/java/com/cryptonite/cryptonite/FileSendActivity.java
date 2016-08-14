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

import Function.Client_FileShare_Send;
import Function.PathPicker;

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

                fileSendTask fs = new fileSendTask();
                fs.execute(files);

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


}


/*
https://android-arsenal.com/details/1/3950
 */