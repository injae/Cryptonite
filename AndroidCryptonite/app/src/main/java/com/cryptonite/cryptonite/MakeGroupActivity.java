package com.cryptonite.cryptonite;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.nio.ByteBuffer;
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
public class MakeGroupActivity extends AppCompatActivity implements PacketRule,OnMapReadyCallback {

        FloatingSearchView searchView;
        ListView listView;
        GroupAdapter adapter;
        ProgressDialog dialog;
        EditText groupname,radiusText;
        ImageButton make;
        long focusLostTime =0;
        LatLng loc;
        CheckBox usegps;
        SlidingUpPanelLayout slidingUpPanelLayout;
        GoogleMap map;
        Marker marker;
        Circle circle;
        Double Radius;


    static final LatLng SEOUL = new LatLng(37.56, 126.97);


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

        usegps = (CheckBox) findViewById(R.id.gps_checkBox);
        radiusText = (EditText) findViewById(R.id.radius);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.Make_group_Panel);
        slidingUpPanelLayout.setTouchEnabled(false);

        make = (ImageButton) findViewById(R.id.make_group);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usegps.isChecked() && loc == null)
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                else
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
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.group_map);
        mapFragment.getMapAsync(this);

        radiusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=0)
                    circle.setRadius(Double.parseDouble(radiusText.getText().toString()));
                else circle.setRadius(0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button setlocation = (Button) findViewById(R.id.btn_set);
        setlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loc = marker.getPosition();
                Radius = Double.valueOf(radiusText.getText().toString());
                new C_Toast(getApplicationContext()).showToast("lat : "+loc.latitude+"\nlng : " +loc.longitude+"\nradius : " + Radius,Toast.LENGTH_SHORT);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        marker = map.addMarker(new MarkerOptions().position(SEOUL));
        circle = map.addCircle(new CircleOptions().radius(Double.parseDouble(radiusText.getText().toString())).strokeWidth(2).strokeColor(Color.BLACK).fillColor(0x3000FF00).center(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                circle.setCenter(latLng);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else if (System.currentTimeMillis() - focusLostTime > 200 )
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
                        byte[] lat = new byte[8];
                        byte[] lng = new byte[8];
                        byte[] radius = new byte[8];

                        event[0] = MAKE_GROUP;
                        event[1] = (byte) (1 + adapter.size() + 3 + 1);
                        event[2] = usegps.isChecked() ? (byte)1 : (byte)0 ;

                        if (usegps.isChecked()) {   //use gps
                            lat = ByteBuffer.wrap(lat).putDouble(loc.latitude).array();
                            lng = ByteBuffer.wrap(lng).putDouble(loc.longitude).array();
                            radius = ByteBuffer.wrap(radius).putDouble(Radius).array();
                        } else {                    //not use gps
                            lat = ByteBuffer.wrap(lat).putDouble(0).array();
                            lng = ByteBuffer.wrap(lng).putDouble(0).array();
                            radius = ByteBuffer.wrap(radius).putDouble(0).array();
                        }

                        css.send.setPacket(event).write();

                        for (int i = 0; i < adapter.size(); i++) {
                            css.send.setPacket(((String) (adapter.getItem(i))).getBytes(), 500).write();
                        }

                        css.send.setPacket(lat, 8).write();
                        css.send.setPacket(lng, 8).write();
                        css.send.setPacket(radius, 8).write();

                        css.send.setPacket(groupname.getText().toString().getBytes(), 500).write();

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

