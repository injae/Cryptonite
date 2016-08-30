package com.cryptonite.cryptonite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;

import Function.Client_Group_Search;

/*
https://github.com/arimorty/floatingsearchview
 */
public class MakeGroupActivity extends AppCompatActivity {

        FloatingSearchView searchView;
        ListView listView;
        ArrayAdapter<String> arrayAdapter;
        ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);


        listView = (ListView) findViewById(R.id.id_listView);

        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.make_group_id_list,R.id.Search_id,arrayList);

        listView.setAdapter(arrayAdapter);

        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        final Client_Group_Search cgs = Client_Group_Search.getInstance(searchView);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

                // Search Id when Query length >= 2
                if (newQuery.length() <=1 ){
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
                    public void onClick(View v) {
                        searchView.clearSuggestions();
                        searchView.clearQuery();
                        searchView.clearSearchFocus();
                        arrayList.add(textView.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }
}
