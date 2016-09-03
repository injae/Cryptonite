package Function;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
        for (int i=0; i<arr.size();i++)
        {
            if (arr.get(i).equals(string))
            {
                new C_Toast(context).showToast("Already in Invite List.",Toast.LENGTH_SHORT);
                return;
            }
        }
        arr.add(string);
        this.notifyDataSetChanged();
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

        view.findViewById(R.id.Search_id_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0)
                    new C_Toast(context).showToast("자신은 그룹에서 제외할 수 없습니다.",Toast.LENGTH_SHORT);
                else {
                    remove(i);
                }

            }
        });

        return view;
    }

}
