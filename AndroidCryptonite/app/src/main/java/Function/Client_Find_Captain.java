package Function;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.cryptonite.cryptonite.GroupMainActivity;

import java.io.IOException;
import java.util.StringTokenizer;


public class Client_Find_Captain extends AsyncTask<String,String,Void> implements PacketRule
{
    // Instance
    private String _choice;
    private Context _context;
    private String _gpname;

    // Another Instance
    private Client_Server_Connector _csc = null;

    // Constructors
    public Client_Find_Captain(Context context)
    {
        _context = context;
        _csc = Client_Server_Connector.getInstance();
    }


    @Override
    protected Void doInBackground(String... strings) { //gpName,Id
        try
        {
            _gpname = strings[0];

            byte[] event = new byte[1024];
            event[0] = FIND_CAPTAIN;
            event[1] = (byte)strings[0].getBytes().length;
            event[2] = (byte)strings[1].getBytes().length;
            Function.frontInsertByte(3, strings[0].getBytes(), event);
            Function.frontInsertByte(3 + strings[0].getBytes().length, strings[1].getBytes(), event);
            _csc.send.setPacket(event).write();

            _choice = new String(_csc.receive.setAllocate(1024).read().getByte()).trim(); // TRUE:$gpCode = CAPTAIN, FALSE:$gpCode = CREW

            publishProgress(_choice);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(String... values) {
        StringTokenizer st = new StringTokenizer(values[0],":");
        st.nextToken();
        String gpCode= st.nextToken();


        Intent intent = new Intent(_context, GroupMainActivity.class);
        intent.putExtra("title",_gpname);
        intent.putExtra("gpCode",gpCode);
        _context.startActivity(intent);
    }
}
