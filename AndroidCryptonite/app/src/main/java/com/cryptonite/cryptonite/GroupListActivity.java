package com.cryptonite.cryptonite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Function.Client_Find_Captain;
import Function.Client_Info;
import Function.Client_Show_Group;
import Function.GroupListAdapter;

public class GroupListActivity extends AppCompatActivity {

    ListView listView;
    GroupListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        listView = (ListView) findViewById(R.id.Group_List_View);
        adapter = new GroupListAdapter(GroupListActivity.this,R.layout.make_group_id_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapter.getItem(i).toString().equals("No Group"))
                    new Client_Find_Captain(GroupListActivity.this).execute((String)adapter.getItem(i), Client_Info.getInstance().getId());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.clear();
        Client_Show_Group csg = new Client_Show_Group(adapter, GroupListActivity.this);
        csg.execute();
    }
}
