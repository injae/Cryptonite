package Function;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.crypto.Cipher;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.PacketProcessor;
import Function.PacketRule;

public class Client_File_Download extends AsyncTask<ArrayList<String>,Integer,Void> implements PacketRule
{
    Crypto _crypto;
    KeyReposit _reposit;
    Context context;
    ProgressDialog progressDialog;
    long fileSize;
    long downSize;


    public Client_File_Download(Context context, ProgressDialog progressDialog){
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(ArrayList<String>... arr) { //download file path
        try
        {
/*            targetpath = targetpath.substring(0,targetpath.length() - 5);
            System.out.println(targetpath);
            _reposit = KeyReposit.getInstance();

            _crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, _reposit.get_aesKey()));
            _crypto.init(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, _reposit.get_aesKey()));*/

            Client_Server_Connector csc = Client_Server_Connector.getInstance();
            Charset cs = Charset.forName("UTF-8");

            for (int i=0; i< arr[0].size();i++) {
                publishProgress(0);
                byte[] event = new byte[1024];
                event[0] = FILE_DOWNLOAD;
                csc.send.setPacket(event).write();

                csc.send.setPacket(cs.encode(arr[0].get(i)).array(), 500).write();

                fileSize = Long.parseLong(new String(csc.receive.setAllocate(500).read().getByte()).trim());
                csc.receive.setAllocate(fileSize);

                StringTokenizer st = new StringTokenizer(arr[0].get(i),"\\");
                while(st.hasMoreTokens())
                    arr[0].set(i,st.nextToken());

                RandomAccessFile raf = new RandomAccessFile(arr[1].get(0).concat("/" + arr[0].get(i)), "rw");
                PacketProcessor p = new PacketProcessor(raf.getChannel(), false);

                p.setAllocate(fileSize);
                while (!csc.receive.isAllocatorEmpty()) {
//                    p.setPacket(_crypto.endecription(csc.receive.read().getByte())).write();
                    p.setPacket(csc.receive.read().getByte()).write();
                    publishProgress(1);
                }
                p.close();

            }
        } catch (NumberFormatException e) {
            // TODO 자동 생성된 catch 블록
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 자동 생성된 catch 블록
            e.printStackTrace();
        }
        publishProgress(2);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        switch (values[0])
        {
            case 0:
                progressDialog.setProgress(0);
                downSize = 0;
                break;
            case 1:
                downSize += 1024;
                progressDialog.setProgress((int)(((double)downSize/(double)fileSize)*100));
                break;
            case 2:
                progressDialog.dismiss();
                break;
        }
    }
}
