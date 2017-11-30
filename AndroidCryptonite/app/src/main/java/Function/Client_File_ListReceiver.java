package Function;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cryptonite.cryptonite.GroupMainActivity;
import com.cryptonite.cryptonite.MainActivity;

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
    private ArrayList<String> files;
    private ArrayList<Integer> keynums;
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
        this.files = new ArrayList<String>();
    }

    @Override
    protected Void doInBackground(String... strings) {  // gpname
        try
        {
            Charset cs = Charset.forName("UTF-8");

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
            keynums = new ArrayList<>();
            publishProgress(0);
            files.clear();
            for(int i = 0; i < _fileCount; i++)
            {

                paths[i] = cs.decode(_csc.receive.setAllocate(1024).read().getByteBuf()).toString().trim();
                files.add(nameTokenizer(paths[i]));
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
                files.add("No Files");
                keynums.add(-1);
                paths = new String[1];
                paths[0] = "No Files";
            }

            dialog.dismiss();
            groupMainActivity.setFilePath(paths,keynums);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        adapter.updateKeynums(keynums);
        adapter.refresh(files);

    }

    private String nameTokenizer(String target)
    {

        StringTokenizer st = new StringTokenizer(target, "\\");
        String temp = null;

        while(st.hasMoreTokens())
        {
            temp = st.nextToken();
        }

        if(temp.endsWith(".cnec")){
            StringTokenizer st2 = new StringTokenizer(temp, "#");
            String filename = "";
            keynums.add(Integer.parseInt(st2.nextToken()));
            while(st2.hasMoreTokens())
            {
                filename = filename + st2.nextToken() + "#";
            }
            filename = filename.substring(0, filename.length()-1);			//마지막 # 떼어냄
            return filename.substring(0,filename.length() - 5);
        } else {
            keynums.add(0);
            return temp.substring(0, temp.length() - 5);
        }

    }
}
