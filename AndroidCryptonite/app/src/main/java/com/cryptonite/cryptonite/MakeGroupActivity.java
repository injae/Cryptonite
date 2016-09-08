package com.cryptonite.cryptonite;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.IOException;
import java.util.List;

import Function.C_Toast;
import Function.Client_Group_Search_Make;
import Function.Client_Info;
import Function.Client_Server_Connector;
import Function.GroupAdapter;
import Function.PacketRule;

/*
https://github.com/arimorty/floatingsearchview
 */
public class MakeGroupActivity extends AppCompatActivity implements PacketRule {

        FloatingSearchView searchView;
        ListView listView;
        GroupAdapter adapter;
        ProgressDialog dialog;
        EditText groupname;
        ImageButton make;
        long focusLostTime =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);


        listView = (ListView) findViewById(R.id.id_listView);

        adapter = new GroupAdapter(getApplicationContext(), R.layout.make_group_id_list);

        listView.setAdapter(adapter);

        adapter.add(Client_Info.getInstance().getId());

        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        final Client_Group_Search_Make cgs = Client_Group_Search_Make.getInstance(searchView);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

                // Search Id when Query length >= 2
                if (newQuery.length() <= 1) {
                    cgs.queue.clear();
                    searchView.clearSuggestions();
                } else {
                    cgs.Search(newQuery);
                }
            }
        });

        searchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, final TextView textView, SearchSuggestion item, int itemPosition) {
                suggestionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchView.clearSuggestions();
                        searchView.clearQuery();
                        searchView.clearSearchFocus();
                        adapter.add(textView.getText().toString());
                    }
                });
            }
        });

        groupname = (EditText) findViewById(R.id.Group_name);

        make = (ImageButton) findViewById(R.id.make_group);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make();
            }
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

            }

            @Override
            public void onFocusCleared() {
                focusLostTime = System.currentTimeMillis();
                Log.d("test",String.valueOf(focusLostTime));
            }
        });



    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - focusLostTime > 200 )
            super.onBackPressed();
    }

    private void make(){

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dialog.dismiss();
                new C_Toast(getApplicationContext()).showToast("Group Make Success!",Toast.LENGTH_LONG);
                finish();
            }
        };



        if(groupname.getText().length() == 0){
            new C_Toast(getApplicationContext()).showToast("Please Enter GroupName", Toast.LENGTH_LONG);
            return;
        } else {

            dialog = ProgressDialog.show(MakeGroupActivity.this,"Making Group","Please Wait",true,false);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Client_Server_Connector css = Client_Server_Connector.getInstance();
                        byte[] event = new byte[1024];

                        event[0] = MAKE_GROUP;
                        event[1] = (byte)(1+ adapter.size() +1);

                        css.send.setPacket(event).write();

                        for(int i=0;i<adapter.size();i++){
                            css.send.setPacket(((String)(adapter.getItem(i))).getBytes(),500).write();
                        }

                        css.send.setPacket(groupname.getText().toString().getBytes(),500).write();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            });
            thread.start();
        }


    }
}

