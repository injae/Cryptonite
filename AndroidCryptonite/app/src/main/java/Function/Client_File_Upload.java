package Function;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cryptonite.cryptonite.GroupMainActivity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Function.PacketRule;
import Function.Function;
import Function.PacketProcessor;

public class Client_File_Upload extends AsyncTask<String,Long,Void> implements PacketRule
{
    // Instance
    private String[] _fileNameArray;
    private String[] _filePathArray;
    private long[] _fileSizeArray;
    private ProgressDialog progressDialog;
    private Context context;
    private long _currentFileSize;
    private long _sendFileSize=0;
    private GroupMainActivity activity;
    private String[] _pbepwd;
    private boolean _usepbe;
    private String pbk;
    private Client_getPBK _cpbe;

    // Another Class
    private Client_Server_Connector _csc = null;

    // Constructors
    public Client_File_Upload(Context context, ProgressDialog progressDialog, GroupMainActivity activity)
    {
        this.activity = activity;
        this.context = context;
        this.progressDialog = progressDialog;
        _csc = Client_Server_Connector.getInstance();
        _cpbe = new Client_getPBK();
    }

    @Override
    protected Void doInBackground(String... strings) { //gpCode
        try
        {
            ByteBuffer[] bb = new ByteBuffer[_fileNameArray.length];
            Charset cs = Charset.forName("UTF-8");
            for(int i = 0; i < _fileNameArray.length; i++)
            {
                Crypto _crypto;
                if (_usepbe == false) {
                    _crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, new Client_Get_Group_Key().running(strings[0], 0)));
                }
                else {

                    pbk = _cpbe.getPBK(_pbepwd[i], Integer.parseInt(strings[0].substring(1)));
                    SecretKey key = new SecretKeySpec(pbk.concat("0000").getBytes(), "AES");
                    _crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, key));

                }



                bb[i] = cs.encode(_fileNameArray[i]);
                byte[] event = new byte[1024];
                event[0] = FILE_UPLOAD;
                event[1] = (byte)_fileNameArray[i].getBytes().length;


                if (_usepbe == false)
                    event[2] = (byte)String.valueOf(_fileSizeArray[i]).getBytes().length;
                else
                    event[2] = (byte)String.valueOf(_fileSizeArray[i]+64).getBytes().length;


                Function.frontInsertByte(3, _fileNameArray[i].getBytes(), event);

                if (_usepbe == false)
                    Function.frontInsertByte(3 + _fileNameArray[i].getBytes().length, String.valueOf(_fileSizeArray[i]).getBytes(), event);
                else
                    Function.frontInsertByte(3 + _fileNameArray[i].getBytes().length, String.valueOf(_fileSizeArray[i]+64).getBytes(), event);

                Function.frontInsertByte(800, strings[0].getBytes(), event);
                _csc.send.setPacket(event).write();

                RandomAccessFile _raf = new RandomAccessFile(_filePathArray[i], "rw");
                FileChannel _fileChannel = _raf.getChannel();
                PacketProcessor p = new PacketProcessor(_fileChannel, false);
                p.setAllocate(_fileSizeArray[i]);

                publishProgress(0L, _fileSizeArray[i]);

                if (_usepbe == true)
                {
                    _csc.send.setAllocate(64);
                    _csc.send.setAllocate(_fileSizeArray[i]);
                    _csc.send.setPacket(_cpbe.SHA(pbk.concat("0000")).getBytes(),64).write();
                }
                else
                    _csc.send.setAllocate(_fileSizeArray[i]);

                while(!p.isAllocatorEmpty())
                {
                    _csc.send.setPacket(_crypto.endecription(p.read().getByte())).write();
                    publishProgress(1L);
                }
                p.close();
                System.out.println(_fileNameArray[i] + " 파일이 전송이 완료되었습니다.");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        publishProgress(2L);
        return null;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        switch (values[0].intValue())
        {
            case 0:
                progressDialog.setProgress(0);
                _currentFileSize = values[1];
                _sendFileSize = 0;
                break;
            case 1:
                _sendFileSize += 1024;
                progressDialog.setProgress((int)(((double)_sendFileSize/(double)_currentFileSize)*100));
                break;
            case 2:
                new C_Toast(context).showToast("Upload Finish", Toast.LENGTH_LONG);
                progressDialog.dismiss();
                activity.refreshList();
                break;
        }
    }

    public Client_File_Upload init(String[] strings, String[] pbepwd,boolean usepbe)
    {
        progressDialog.show();
        _filePathArray = strings.clone();
        _fileNameArray = new String[strings.length];
        _pbepwd = pbepwd;
        _usepbe = usepbe;

        for (int i=0; i<strings.length;i++)
        {
            StringTokenizer st = new StringTokenizer(strings[i],"/");

            while(st.hasMoreTokens())
            {
                _fileNameArray[i] = st.nextToken();
            }
            if (usepbe)
                _fileNameArray[i] = _fileNameArray[i].concat(".cnmc");
            else
                _fileNameArray[i] = _fileNameArray[i].concat(".cnec");
        }

        _fileSizeArray = new long[_filePathArray.length];
        for(int i = 0; i < _filePathArray.length; i++)
        {
            File temp = new File(_filePathArray[i]);
            _fileSizeArray[i] = temp.length();
        }

        return this;
    }
}
