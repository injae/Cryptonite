package Function;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.PacketProcessor;
import Function.PacketRule;

public class Client_File_Download extends AsyncTask<ArrayList<String>,Integer,Integer> implements PacketRule
{
    Crypto _crypto;
    KeyReposit _reposit;
    Context context;
    ProgressDialog progressDialog;
    long fileSize;
    long downSize;
    String _gpCode;


    public Client_File_Download(Context context, ProgressDialog progressDialog,String gpCode){
        this.context = context;
        this.progressDialog = progressDialog;
        _gpCode = gpCode;
    }

    @Override
    protected Integer doInBackground(ArrayList<String>... arr) { //download file path, localPath
        try
        {

            for (int i=0; i< arr[0].size();i++) {

                SecretKey key = new Client_Get_Group_Key().running(_gpCode, getKeynum(arr[0].get(i)));
                _crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));
                _crypto.init(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));

                Client_Server_Connector csc = Client_Server_Connector.getInstance();
                Charset cs = Charset.forName("UTF-8");


                publishProgress(0);
                byte[] event = new byte[1024];
                event[0] = FILE_DOWNLOAD;
                csc.send.setPacket(event).write();

                csc.send.setPacket(cs.encode(arr[0].get(i)).array(), 500).write();

                String tmpfileSize = new String(csc.receive.setAllocate(500).read().getByte()).trim();
                if (tmpfileSize.equals("distance"))
                {
                    publishProgress(3);
                    return 1;
                }
                else if(tmpfileSize.equals("timeover"))
                {
                    publishProgress(4);
                    return 2;
                }

                fileSize = Long.parseLong(tmpfileSize);
                csc.receive.setAllocate(fileSize);

                StringTokenizer st = new StringTokenizer(arr[0].get(i),"\\");
                while(st.hasMoreTokens())
                    arr[0].set(i,st.nextToken());

                String localpath = arr[1].get(0).concat("/" + arr[0].get(i));
                RandomAccessFile raf = new RandomAccessFile(localpath.substring(0,localpath.length()-5), "rw");
                PacketProcessor p = new PacketProcessor(raf.getChannel(), false);

                p.setAllocate(fileSize);
                while (!csc.receive.isAllocatorEmpty()) {
                    p.setPacket(_crypto.endecription(csc.receive.read().getByte())).write();
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        publishProgress(2);
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
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
                new C_Toast(context).showToast("Download complete!",Toast.LENGTH_LONG);
                break;
            case 3:
                progressDialog.dismiss();
                new C_Toast(context).showToast("Please send your loaction.", Toast.LENGTH_LONG);
                break;
            case 4:
                progressDialog.dismiss();
                new C_Toast(context).showToast("Please resend your loaction.\n (10 minutes after the last location has passed.)",Toast.LENGTH_LONG);
                break;
        }
    }

    private int getKeynum(String filePath) {
        String fileName = "";
        int keynum = 0;
        if (!filePath.endsWith(".cnmc")) {

            StringTokenizer st = new StringTokenizer(filePath, "\\");

            while (st.hasMoreTokens()) {
                fileName = st.nextToken();

            }

            StringTokenizer st2 = new StringTokenizer(fileName, "#");
            String filename = "";
            keynum = Integer.parseInt(st2.nextToken());

        }
        return keynum;
    }
}
