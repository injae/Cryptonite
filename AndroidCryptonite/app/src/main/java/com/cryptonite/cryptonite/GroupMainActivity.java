package com.cryptonite.cryptonite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Iterator;

import Function.C_Toast;
import Function.Client_File_Download;
import Function.Client_File_ListReceiver;
import Function.Client_File_Upload;
import Function.Client_Group_Invite;
import Function.Client_Group_Search_Invite;
import Function.FileListAdapter;
import Function.GroupInviteAdapter;
import Function.PathPicker;


// https://github.com/umano/AndroidSlidingUpPanel
public class GroupMainActivity extends AppCompatActivity {

    String groupName;
    ListView fileList,InviteList;
    FileListAdapter adapter;
    String gpCode;
    GroupMainActivity activity;
    String[] filePath;
    ArrayList<String> selectPath;
    SlidingUpPanelLayout slidingUpPanelLayout;
    FloatingSearchView floatingSearchView;
    GroupInviteAdapter groupInviteAdapter;
    Client_Group_Search_Invite cgs;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        activity = this;
        groupInviteAdapter = new GroupInviteAdapter(getApplicationContext(),R.layout.make_group_id_list);


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
                if (!selectPath.contains(filePath[i])) {
                    selectPath.add(filePath[i]);
                    view.setBackgroundColor(Color.parseColor("#02aeef"));
                }
                else {
                    selectPath.remove(filePath[i]);
                    view.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }

            }
        });

        InviteList = (ListView) findViewById(R.id.id_listView2);

        InviteList.setAdapter(groupInviteAdapter);
        groupInviteAdapter.add("No Invite List");

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.Add_User_Pane);

        floatingSearchView = (FloatingSearchView) findViewById(R.id.invite_search_view);

        cgs = Client_Group_Search_Invite.getInstance(floatingSearchView);

        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (newQuery.length() <= 1) {
                    cgs.queue.clear();
                    floatingSearchView.clearSuggestions();
                } else {
                    cgs.Search(newQuery);
                }
            }
        });

        floatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,final TextView textView, SearchSuggestion item, int itemPosition) {
                suggestionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        floatingSearchView.clearSuggestions();
                        floatingSearchView.clearQuery();
                        floatingSearchView.clearSearchFocus();
                        groupInviteAdapter.add(textView.getText().toString());
                    }
                });
            }
        });

        ImageButton invite = (ImageButton) findViewById(R.id.invite_Button);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupInviteAdapter.getItem(0).equals("No Invite List"))
                    new C_Toast(getApplicationContext()).showToast("Please add id to invite",Toast.LENGTH_SHORT);
                else
                {
                    progressDialog = new ProgressDialog(GroupMainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Inviting");
                    progressDialog.show();
                    String[] temp = new String[1 + groupInviteAdapter.size() + 1];
                    temp[0]=String.valueOf(groupInviteAdapter.size());
                    for(int i=0; i<groupInviteAdapter.size();i++)
                        temp[1+i] = (String)groupInviteAdapter.getItem(i);
                    temp[groupInviteAdapter.size()+1]=gpCode;
                    new Client_Group_Invite(getApplicationContext(),progressDialog).execute(temp);
                }
            }
        });

        ImageButton addUser = (ImageButton) findViewById(R.id.Add_User);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        ImageButton fileDown = (ImageButton) findViewById(R.id.File_Download);

        fileDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectPath.isEmpty())
                    new C_Toast(GroupMainActivity.this).showToast("Select files to download",Toast.LENGTH_SHORT);
                else {
                    if (selectPath.get(0).equals("No Files")){
                        new C_Toast(getApplicationContext()).showToast("No Files in group folder",Toast.LENGTH_SHORT);
                        return;
                    }
                    new C_Toast(GroupMainActivity.this).showToast("Select download Folder",Toast.LENGTH_SHORT);
                    PathPicker p = new PathPicker(GroupMainActivity.this,PathPicker.Select_Dir);
                    p.setDialogSelectionListener(new DialogSelectionListener() {
                        @Override
                        public void onSelectedFilePaths(String[] files) {
                            if (files.length == 0)
                                new C_Toast(GroupMainActivity.this).showToast("Dir doesn't selected!!",Toast.LENGTH_LONG);
                            else {
                                ProgressDialog progressDialog = new ProgressDialog(GroupMainActivity.this);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog.setTitle("Downloading...");
                                progressDialog.setMax(100);
                                progressDialog.setCancelable(false);
                                ArrayList<String> localPath = new ArrayList<String>();
                                localPath.add(files[0]);
                                progressDialog.show();
                                new Client_File_Download(getApplicationContext(),progressDialog).execute(selectPath,localPath);
                            }
                        }
                    });
                    p.show();
                }
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

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
            super.onBackPressed();
    }

    public void refreshList() {
        new Client_File_ListReceiver(GroupMainActivity.this,adapter,this).execute(gpCode);
    }

    public void setFilePath(String[] path) {
        this.filePath = path;
        this.selectPath = new ArrayList<String>();
    }
}
