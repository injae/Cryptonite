package com.cryptonite.cryptonite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import Function.Client_File_ListReceiver;
import Function.FileListAdapter;

public class GroupMainActivity extends AppCompatActivity {

    String groupName;
    ListView fileList;
    FileListAdapter adapter;
    String gpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Client_File_ListReceiver(GroupMainActivity.this,adapter).execute(groupName);


    }
}
