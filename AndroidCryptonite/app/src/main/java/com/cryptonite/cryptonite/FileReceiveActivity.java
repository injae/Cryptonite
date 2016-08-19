package com.cryptonite.cryptonite;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private TextView path;
    private EditText otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_receive);

        Button receiveButton = (Button) findViewById(R.id.receive);

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileReceive();
            }
        });
        otp = (EditText) findViewById(R.id.OTP);

        path = (TextView) findViewById(R.id.path);
        path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dirChoose();
            }
        });
    }

    private void FileReceive()
    {
        fileReceiveTask fr = new fileReceiveTask();
        fr.execute(path.getText().toString(),otp.getText().toString());
    }


    private void dirChoose()
    {
        PathPicker p = new PathPicker(this,PathPicker.Select_Dir);
        p.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(files.length!=0)
                    path.setText(files[0]);
                else {
                    new C_Toast(getApplicationContext()).showToast("Dir doesn't selected", Toast.LENGTH_LONG);
                }
            }
        });
        p.show();
    }




    class fileReceiveTask extends AsyncTask<String,Integer,Boolean>
    {
        Client_FileShare_Receive cfr;
        fileReceiveTask()
        {
            cfr = new Client_FileShare_Receive();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("File",params[0]);
            cfr.receiveFiles(params[0],params[1]);
            return null;
        }
    }

}
