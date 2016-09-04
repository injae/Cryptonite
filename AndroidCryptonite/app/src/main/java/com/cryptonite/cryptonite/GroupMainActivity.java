package com.cryptonite.cryptonite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GroupMainActivity extends AppCompatActivity {

     String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        Intent intent = getIntent();
        groupName = intent.getStringExtra("title");

        TextView title = (TextView) findViewById(R.id.Group_main_title);
        title.setText(groupName);
    }
}
