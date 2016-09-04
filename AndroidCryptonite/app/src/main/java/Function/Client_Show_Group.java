package Function;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cryptonite.cryptonite.GroupListActivity;
import com.cryptonite.cryptonite.R;

import java.io.IOException;
import java.util.ArrayList;


public class Client_Show_Group extends AsyncTask<Void, Integer, Void> implements PacketRule
{
    // Instance
    private Client_Server_Connector _csc = null;
    private String _id;
    private int _groupCount;
    private GroupListAdapter _adapter;
    private ProgressDialog dialog;
    private Context _context;

    // Constructors
    public Client_Show_Group(GroupListAdapter adapter, Context context)
    {
        _csc = Client_Server_Connector.getInstance();
        _adapter = adapter;
        _context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try
        {
            publishProgress(0);

            _id = Client_Info.getInstance().getId();

            byte[] event = new byte[1024];
            event[0] = SHOW_GROUP;
            event[1] = (byte)_id.getBytes().length;
            Function.frontInsertByte(2, _id.getBytes(), event);
            _csc.send.setPacket(event).write();

            _groupCount = Integer.parseInt(new String(_csc.receive.setAllocate(100).read().getByte()).trim());

            for(int i = 0; i < _groupCount; i++)
            {
                _adapter.add(new String(_csc.receive.setAllocate(1024).read().getByte()).trim());
            }

            publishProgress(1);


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... params) {
        switch (params[0])
        {
            case 0:
                dialog = ProgressDialog.show(_context,"Loading","Loading Group List",true,false);
                _adapter.clear();
                _adapter.notifyDataSetChanged();
                break;
            case 1:
                if (_groupCount==0)
                    _adapter.add("No Group");
                _adapter.notifyDataSetChanged();
            case 2:
                dialog.dismiss();
                break;
        }
    }
}
