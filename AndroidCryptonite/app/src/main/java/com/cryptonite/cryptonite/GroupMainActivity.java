package com.cryptonite.cryptonite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;

import Function.C_Toast;
import Function.Client_File_ListReceiver;
import Function.Client_File_Upload;
import Function.FileListAdapter;
import Function.PathPicker;

public class GroupMainActivity extends AppCompatActivity {

    String groupName;
    ListView fileList;
    FileListAdapter adapter;
    String gpCode;
    GroupMainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        activity = this;

        Intent intent = getIntent();
        groupName = intent.getStringExtra("title");
        gpCode = intent.getStringExtra("gpCode");


        TextView title = (TextView) findViewById(R.id.Group_main_title);
        title.setText(groupName);

        fileList = (ListView) findViewById(R.id.File_ListView);
        adapter = new FileListAdapter(GroupMainActivity.this,R.layout.make_group_id_list);

        fileList.setAdapter(adapter);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Test","tttt");
            }
        });

        ImageButton fileSend = (ImageButton) findViewById(R.id.File_Send);

        fileSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(GroupMainActivity.this).create();
                dialog.setCancelable(true);
                dialog.setTitle("Choose Files To Upload");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PathPicker pathPicker = new PathPicker(GroupMainActivity.this,PathPicker.Select_Files);
                        pathPicker.setDialogSelectionListener(new DialogSelectionListener() {
                            @Override
                            public void onSelectedFilePaths(String[] files) {
                                if (files.length == 0)
                                {
                                    new C_Toast(GroupMainActivity.this).showToast("Files doesn't selected", Toast.LENGTH_SHORT);
                                }
                                else
                                {
                                    ProgressDialog progressDialog = new ProgressDialog(GroupMainActivity.this);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progressDialog.setTitle("Uploading...");
                                    progressDialog.setMax(100);
                                    progressDialog.setCancelable(false);
                                    new Client_File_Upload(GroupMainActivity.this,progressDialog,activity).init(files).execute(gpCode);
                                }
                            }
                        });
                        pathPicker.show();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();
    }

    public void refreshList() {
        new Client_File_ListReceiver(GroupMainActivity.this,adapter).execute(groupName);
    }
}
