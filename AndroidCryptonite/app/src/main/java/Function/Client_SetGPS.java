package Function;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by 전용범 on 2016-09-20.
 */
public class Client_SetGPS extends AsyncTask<Double,Void,Void> implements PacketRule {

    Client_Server_Connector css;
    Context context;
    public Client_SetGPS(Context context) {
        this.context = context;
        css = Client_Server_Connector.getInstance();    //get SocketChannel
    }


    @Override
    protected Void doInBackground(Double... doubles) {   //lat,lng
        try {

            byte[] event = new byte[1024];
            event[0] = SET_GPS;
            event[1] = 3;

            byte[] lat = new byte[8];
            byte[] lng = new byte[8];

            ByteBuffer.wrap(lat).putDouble(doubles[0]);
            ByteBuffer.wrap(lng).putDouble(doubles[1]);

            css.send.setPacket(event).write();  //send setGPS event
            css.send.setPacket(lat,8).write();  //send lat
            css.send.setPacket(lng,8).write();  //send lng

            Log.d("TEst", String.valueOf(doubles[0]) + doubles[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        new C_Toast(context).showToast("Loaction Sent", Toast.LENGTH_SHORT);
    }
}
