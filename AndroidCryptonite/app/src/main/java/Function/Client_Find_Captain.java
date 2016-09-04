package Function;

import android.os.AsyncTask;

import java.io.IOException;


public class Client_Find_Captain extends AsyncTask<String,Void,Void> implements PacketRule
{
    // Instance
    private String _choice;

    // Another Instance
    private Client_Server_Connector _csc = null;

    // Constructors
    public Client_Find_Captain()
    {
        _csc = Client_Server_Connector.getInstance();
    }


    @Override
    protected Void doInBackground(String... strings) { //gpName,Id
        try
        {
            byte[] event = new byte[1024];
            event[0] = FIND_CAPTAIN;
            event[1] = (byte)strings[0].getBytes().length;
            event[2] = (byte)strings[1].getBytes().length;
            Function.frontInsertByte(3, strings[0].getBytes(), event);
            Function.frontInsertByte(3 + strings[0].getBytes().length, strings[1].getBytes(), event);
            _csc.send.setPacket(event).write();

            _choice = new String(_csc.receive.setAllocate(1024).read().getByte()).trim(); // TRUE:$gpCode = CAPTAIN, FALSE:$gpCode = CREW
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
