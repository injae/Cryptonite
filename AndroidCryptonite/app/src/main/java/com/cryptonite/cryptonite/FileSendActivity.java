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

public class FileSendActivity extends AppCompatActivity {

    FilePickerDialog dialog;
    String[] filepaths;

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

        DialogProperties properties=new DialogProperties();

        properties.selection_mode= DialogConfigs.MULTI_MODE;
        properties.selection_type=DialogConfigs.FILE_SELECT;
        properties.root=new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions=null;

        dialog = new FilePickerDialog(FileSendActivity.this,properties);
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                filepaths = files;
                fileSendTask fst = new fileSendTask();
                fst.execute((Void)null);
            }
        });

        dialog.show();
    }

    class fileSendTask extends AsyncTask<Void,Integer,Boolean>
    {
        Client_FileShare_Send cfs;
        fileSendTask()
        {
            cfs = new Client_FileShare_Send();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            cfs.sendFile(filepaths);
            return null;
        }
    }


}


/*
https://android-arsenal.com/details/1/3950
 */