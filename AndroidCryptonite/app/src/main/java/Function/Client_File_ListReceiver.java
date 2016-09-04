package Function;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Client_File_ListReceiver extends AsyncTask<String,Integer,Void> implements PacketRule
{
    // Instance
    private int _fileCount;
    private  FileListAdapter adapter;
    private ProgressDialog dialog;
    private Context context;
    // Another Class
    private Client_Server_Connector _csc = null;

    // Constructors
    public Client_File_ListReceiver(Context context, FileListAdapter adapter)
    {
        this.context = context;
        this.adapter = adapter;
        _csc = Client_Server_Connector.getInstance();
    }

    @Override
    protected Void doInBackground(String... strings) {  // gpname
        try
        {
            publishProgress(0);
            byte[] event = new byte[1024];
            event[0] = FILE_LIST_REQUEST;
            event[1] = 1; // group mode
            byte[] temp = strings[0].getBytes();
            for(int i = 0; i < strings[0].getBytes().length; i++)
            {
                event[i + 2] = temp[i];
            }

            _csc.send.setPacket(event).write();

            _fileCount = Integer.parseInt(new String(_csc.receive.setAllocate(100).read().getByte()).trim());
            for(int i = 0; i < _fileCount; i++)
            {
                adapter.add(nameTokenizer(new String(_csc.receive.setAllocate(1024).read().getByte()).trim()));
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
    protected void onProgressUpdate(Integer... values) {
        if (values[0] == 0)
        {
            dialog = ProgressDialog.show(context,"Loading..","Receiving File List",true,false);
        } else if(values[0] == 1) {
            if (_fileCount == 0)
                adapter.add("No Files");
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    private String nameTokenizer(String target)
    {
        StringTokenizer st = new StringTokenizer(target, "\\");
        String temp = null;;

        while(st.hasMoreTokens())
        {
            temp = st.nextToken();
        }

        return temp;
    }
}
