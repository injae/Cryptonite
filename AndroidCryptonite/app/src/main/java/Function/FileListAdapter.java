package Function;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptonite.cryptonite.GroupMainActivity;
import com.cryptonite.cryptonite.R;

import java.util.ArrayList;

/**
 * Created by 전용범 on 2016-09-01.
 */
public class FileListAdapter extends BaseAdapter {

    ArrayList<String> arr;
    LayoutInflater inf;
    Context context;
    int layout;

    public FileListAdapter(Context context, int layout) {
        arr = new ArrayList<>();

        this.context = context;
        this.layout = layout;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(String string){
        arr.add(string);
    }

    public void remove(int i){
        arr.remove(i);
        this.notifyDataSetChanged();
    }

    public void clear(){
        arr.clear();
    }

    public String[] list(){
        return (String[]) arr.toArray();
    }

    public int size(){
        return arr.size();
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null)
            view = inf.inflate(layout, null);

        TextView t = (TextView) view.findViewById(R.id.Search_id_textView);
        t.setText(arr.get(i));

        view.findViewById(R.id.Search_id_Button).setVisibility(View.GONE);

        return view;
    }

}
