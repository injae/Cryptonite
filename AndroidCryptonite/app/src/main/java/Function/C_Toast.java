package Function;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cryptonite.cryptonite.R;

public class C_Toast extends Toast {
    Context mContext;
    public C_Toast(Context context) {
        super(context);
        mContext = context;
    }

    public void showToast(String body, int duration){
        // http://developer.android.com/guide/topics/ui/notifiers/toasts.html
        LayoutInflater inflater;
        View v;

        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.toast_layout, null);

        TextView text = (TextView) v.findViewById(R.id.toast_context);
        text.setText(body);

        show(this,v,duration);
    }

    private void show(Toast toast, View v, int duration){
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.TOP, 0, 250);
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }

}
