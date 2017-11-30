package Function;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

    ArrayList<String> arr,arr1;
    LayoutInflater inf;
    Context context;
    int layout;
    public ArrayList<Integer> keynums;

    public FileListAdapter(Context context, int layout) {
        arr = new ArrayList<>();
        arr1 = new ArrayList<>();
        keynums = new ArrayList<>();
        this.context = context;
        this.layout = layout;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(String string){
        arr1.add(string);
    }

    public void remove(int i){
        arr1.remove(i);
        this.notifyDataSetChanged();
    }

    public void refresh(ArrayList<String> array){
        if (array == null)
            array = new ArrayList<String>();
        arr.addAll(array);
        this.notifyDataSetChanged();
    }

    public void clear(){
        arr = new ArrayList<>();
        notifyDataSetChanged();
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

        if (keynums.get(i) == 0){
            view.setBackgroundColor(Color.parseColor("#9aed89"));
        }

        return view;
    }

    public void updateKeynums(ArrayList<Integer> keynums){
        this.keynums.addAll(keynums);
    }

}
