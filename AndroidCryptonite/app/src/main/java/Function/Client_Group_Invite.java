package Function;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import Function.PacketRule;
import Function.Function;

public class Client_Group_Invite extends AsyncTask<String,ArrayList<String>,Void> implements PacketRule
{
    // Instance
    private String _check;
    private ArrayList<String> invited;
    private Context context;
    private ProgressDialog progressDialog;

    // Another Class Instance
    private Client_Server_Connector _csc = null;

    // Constructors
    public Client_Group_Invite(Context context, ProgressDialog progressDialog)
    {
        this.context = context;
        this.progressDialog = progressDialog;
        _csc = Client_Server_Connector.getInstance();
    }

    @Override
    protected Void doInBackground(String... strings) {  //idCount,id,gpCode

        invited = new ArrayList<String>();
        int count = Integer.parseInt(strings[0]);
        String[] ids = new String[count];
        String gpCode = strings[count + 1];
        for (int i=0;i<count;i++)
            ids[i]=strings[i+1];

        for (int i =0;i<count;i++) {
            try {
                byte[] event = new byte[1024];
                event[0] = GROUP_INVITE;
                event[1] = (byte) ids[i].getBytes().length;
                event[2] = (byte) gpCode.getBytes().length;
                Function.frontInsertByte(3, ids[i].getBytes(), event);
                Function.frontInsertByte(3 + ids[i].getBytes().length, gpCode.getBytes(), event);
                _csc.send.setPacket(event).write();
                _check = new String(_csc.receive.setAllocate(100).read().getByte()).trim();
                if (_check.equals("TRUE"))
                    invited.add(ids[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        publishProgress(invited);
        return null;
        }

    @Override
    protected void onProgressUpdate(ArrayList<String>... values) {
        if (values[0].size()==0)
            new C_Toast(context).showToast("Already Invited", Toast.LENGTH_SHORT);
        else
        {
            String success = new String();
            success = success.concat(values[0].get(0));
            for (int i = 1; i< values[0].size();i++)
                success = success.concat(", " + values[0].get(i));
            new C_Toast(context).showToast(success + " invited",Toast.LENGTH_LONG);
        }
        progressDialog.dismiss();
    }
}
