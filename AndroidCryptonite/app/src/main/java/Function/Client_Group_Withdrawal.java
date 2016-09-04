package Function;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.StringTokenizer;

import Function.Function;
import Function.PacketRule;

public class Client_Group_Withdrawal extends AsyncTask<String,Integer,Void> implements PacketRule
{

    GroupListAdapter adapter;
    int i;

    public Client_Group_Withdrawal(GroupListAdapter adapter, int i)
    {
        this.adapter = adapter;
        this.i = i;
    }


    @Override
    protected Void doInBackground(String... strings) { //gpname,Id
        try
        {
            Client_Server_Connector csc = Client_Server_Connector.getInstance();

            byte[] event = new byte[1024];
            event[0] = FIND_CAPTAIN;
            event[1] = (byte)strings[0].getBytes().length;
            event[2] = (byte)strings[1].getBytes().length;
            Function.frontInsertByte(3, strings[0].getBytes(), event);
            Function.frontInsertByte(3 + strings[0].getBytes().length, strings[1].getBytes(), event);
            csc.send.setPacket(event).write();

            String choice = new String(csc.receive.setAllocate(1024).read().getByte()).trim(); // TRUE:$gpCode = CAPTAIN, FALSE:$gpCode = CREW

            StringTokenizer st = new StringTokenizer(choice,":");
            st.nextToken();
            String gpCode = st.nextToken();


            event = new byte[1024];
            event[0] = GROUP_WITHDRAWAL;
            Function.frontInsertByte(1, gpCode.getBytes(), event);
            csc.send.setPacket(event).write();

            publishProgress(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        adapter.remove(i);
        if (adapter.size()==0) {
            adapter.add("No Group");
            adapter.notifyDataSetChanged();
        }
    }
}
