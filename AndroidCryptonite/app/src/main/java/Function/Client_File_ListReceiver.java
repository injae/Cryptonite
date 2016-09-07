package Function;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.cryptonite.cryptonite.GroupMainActivity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Client_File_ListReceiver extends AsyncTask<String,Integer,Void> implements PacketRule
{
    // Instance
    private int _fileCount;
    private  FileListAdapter adapter;
    private ProgressDialog dialog;
    private Context context;
    private String[] paths;
    private GroupMainActivity groupMainActivity;
    // Another Class
    private Client_Server_Connector _csc = null;

    // Constructors
    public Client_File_ListReceiver(Context context, FileListAdapter adapter, GroupMainActivity groupMainActivity)
    {
        this.context = context;
        this.adapter = adapter;
        this.groupMainActivity = groupMainActivity;
        _csc = Client_Server_Connector.getInstance();
    }

    @Override
    protected Void doInBackground(String... strings) {  // gpname
        try
        {
            Charset cs = Charset.forName("UTF-8");
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
            paths = new String[_fileCount];
            for(int i = 0; i < _fileCount; i++)
            {
                paths[i] = cs.decode(_csc.receive.setAllocate(1024).read().getByteBuf()).toString().trim();
                adapter.add(nameTokenizer(paths[i]));
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
            adapter.clear();
            dialog = ProgressDialog.show(context,"Loading..","Receiving File List",true,false);
        } else if(values[0] == 1) {
            if (_fileCount == 0) {
                adapter.add("No Files");
                paths = new String[1];
                paths[0] = "No Files";
            }
            adapter.refresh();
            dialog.dismiss();
            groupMainActivity.setFilePath(paths);
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
