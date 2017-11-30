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
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import Function.C_Toast;
import Function.Client_File_Download;
import Function.Client_File_ListReceiver;
import Function.Client_File_Upload;
import Function.Client_Group_Invite;
import Function.Client_Group_Search_Invite;
import Function.Client_Info;
import Function.FileListAdapter;
import Function.GroupInviteAdapter;
import Function.PathPicker;


// https://github.com/umano/AndroidSlidingUpPanel
public class GroupMainActivity extends AppCompatActivity {

    String groupName;
    ListView fileList,InviteList;
    FileListAdapter adapter;
    String gpCode;
    boolean captain;
    boolean usepbe = false;
    String[] pbepwd;
    GroupMainActivity activity;
    String[] filePath;
    ArrayList<String> selectPath;
    ArrayList<Integer> selectKeynum;
    ArrayList<String> password;
    SlidingUpPanelLayout slidingUpPanelLayout;
    FloatingSearchView floatingSearchView;
    GroupInviteAdapter groupInviteAdapter;
    Client_Group_Search_Invite cgs;
    ProgressDialog progressDialog;
    String pwd;
    ArrayList<Integer> keynums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        activity = this;
        groupInviteAdapter = new GroupInviteAdapter(getApplicationContext(),R.layout.make_group_id_list);


        Intent intent = getIntent();
        groupName = intent.getStringExtra("title");
        captain = intent.getBooleanExtra("captain",false);
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
                    selectKeynum.add(adapter.keynums.get(i));
                    view.setBackgroundColor(Color.parseColor("#02aeef"));
                }
                else {
                    selectPath.remove(filePath[i]);
                    if (keynums.get(i)!=0)
                        view.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    else
                        view.setBackgroundColor(Color.parseColor("#9aed89"));
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

        if (!captain)
            addUser.setVisibility(View.GONE);

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

                                ArrayList<String> localPath = new ArrayList<String>();
                                localPath.add(files[0]);
                                download(selectPath,selectKeynum,0,localPath);




 /*                               ProgressDialog progressDialog = new ProgressDialog(GroupMainActivity.this);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog.setTitle("Downloading...");
                                progressDialog.setMax(100);
                                progressDialog.setCancelable(false);
                                ArrayList<String> localPath = new ArrayList<String>();
                                localPath.add(files[0]);
                                progressDialog.show();
                                new Client_File_Download(getApplicationContext(),progressDialog,gpCode).execute(selectPath,localPath);*/
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

                final AlertDialog dialog1 = new AlertDialog.Builder(activity).create();
                dialog1.setCancelable(true);
                dialog1.setTitle("Use PBE to encrypt file?");
                dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usepbe = true;
                        dialog1.dismiss();
                        send();
                    }
                });
                dialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usepbe = false;
                        dialog1.dismiss();
                        send();
                    }
                });
                dialog1.show();
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

    public void setFilePath(String[] path, ArrayList<Integer> keynums) {        //파일 리스트 받아오면 실행
        this.filePath = path;
        this.selectPath = new ArrayList<String>();
        this.selectKeynum = new ArrayList<Integer>();
        this.keynums = keynums;
        this.password = new ArrayList<>();
    }

    private void send(){
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
                    if (usepbe == true)
                    {
                        pbepwd = new String[files.length];
                        getpwd(files,0);
                    } else {
                        ProgressDialog progressDialog = new ProgressDialog(GroupMainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.setMax(100);
                        progressDialog.setCancelable(false);
                        new Client_File_Upload(GroupMainActivity.this,progressDialog,activity).init(files,new String[1],usepbe).execute(gpCode);
                    }
                }
            }
        });
        pathPicker.show();
    }

    private void download(ArrayList<String> name, ArrayList<Integer> keynum, int num,ArrayList<String> localPath){
        final ArrayList<String> _name = (ArrayList<String>) name.clone();
        final ArrayList<Integer> _keynum = (ArrayList<Integer>) keynum.clone();
        final int _num = num;
        final ArrayList<String> _localPath = localPath;
        if (keynum.get(num) == 0)               //pbe
        {
            LayoutInflater layoutInflater = LayoutInflater.from(GroupMainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.test_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GroupMainActivity.this);
            alertDialogBuilder.setView(promptView);

            TextView textView = (TextView) promptView.findViewById(R.id.textView);
            textView.setText("Input " + nameTokenizer(name.get(num)) + " Password");
            final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            password.add(editText.getText().toString());
                            System.out.println(password.get(_num));
                        }
                    });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (_name.size() > (_num + 1)) {
                        download(_name, _keynum,_num+1,_localPath);
                    } else {
                        ProgressDialog progressDialog = new ProgressDialog(GroupMainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("Downloading...");
                        progressDialog.setMax(100);
                        progressDialog.setCancelable(false);
                        new Client_File_Download(getApplicationContext(),progressDialog,gpCode).execute(selectPath,_localPath,password);
                    }
                }
            });
            alert.show();
        } else {
            password.add("0");
            if (_name.size() > (_num + 1)) {
                download(_name,_keynum,_num+1,_localPath);
            } else {
                ProgressDialog progressDialog = new ProgressDialog(GroupMainActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("Downloading...");
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                new Client_File_Download(getApplicationContext(),progressDialog,gpCode).execute(selectPath,_localPath,password);
            }
        }
    }

    private void getpwd(String[] name,int num){
        // get prompts.xml view
        final String[] files = name.clone();
        final int index = num;
        String filename = name[num];

        StringTokenizer st = new StringTokenizer(filename,"/");
        while(st.hasMoreTokens()){
            filename = st.nextToken();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(GroupMainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.test_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GroupMainActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView textView = (TextView) promptView.findViewById(R.id.textView);
        textView.setText("Input " + filename + " Password");
        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pbepwd[index] = editText.getText().toString();
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (files.length > (index + 1))
                {
                    getpwd(files,index+1);
                } else{
                    ProgressDialog progressDialog = new ProgressDialog(GroupMainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.setMax(100);
                    progressDialog.setCancelable(false);
                    new Client_File_Upload(GroupMainActivity.this,progressDialog,activity).init(files,pbepwd,usepbe).execute(gpCode);
                }
            }
        });
        alert.show();

    }

    private String nameTokenizer(String target)
    {

        StringTokenizer st = new StringTokenizer(target, "\\");
        String temp = null;

        while(st.hasMoreTokens())
        {
            temp = st.nextToken();
        }

        if(temp.endsWith(".cnec")){
            StringTokenizer st2 = new StringTokenizer(temp, "#");
            st2.nextToken();
            String filename = "";
            keynums.add(Integer.parseInt(st2.nextToken()));
            while(st2.hasMoreTokens())
            {
                filename = filename + st2.nextToken() + "#";
            }
            filename = filename.substring(0, filename.length()-1);			//마지막 # 떼어냄
            return filename.substring(0,filename.length() - 5);
        } else {
            keynums.add(0);
            return temp.substring(0, temp.length() - 5);
        }

    }
}
