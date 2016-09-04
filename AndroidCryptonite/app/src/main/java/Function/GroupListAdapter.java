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
public class GroupListAdapter extends BaseAdapter {

    ArrayList<String> arr;
    LayoutInflater inf;
    Context context;
    int layout;

    public GroupListAdapter(Context context, int layout) {
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

        if (i == 0 && arr.get(0).equals("No Group"))
        {
            view.findViewById(R.id.Search_id_Button).setVisibility(View.GONE);
        }

        final GroupListAdapter adapter = this;

        view.findViewById(R.id.Search_id_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setMessage("Are you sure you want to remove Group?");
                dialog.setCancelable(true);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        new Client_Group_Withdrawal(adapter,i).execute(arr.get(i),Client_Info.getInstance().getId());
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

        return view;
    }

}
