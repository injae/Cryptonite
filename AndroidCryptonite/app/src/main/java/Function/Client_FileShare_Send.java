package Function;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.cryptonite.cryptonite.FileSendActivity;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;
import java.io.*;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 *
 * Name : FileShare
 * Description : It can share file
 *
 * */

public class Client_FileShare_Send extends AsyncTask<String,Integer,Boolean> implements PacketRule
{
    // Instance
    private String _OTP = null;
    public static boolean isSending = false;

    // About Files
    private FileChannel _fileChannel = null;
    private RandomAccessFile _raf = null;

    private String[] _fileNameArray = null;
    private int[] _fileNameSize = null;

    private File _tempFile = null;
    private long[] _fileSizeArray = null;
    private long _upSize =0;
    // Another Class Instance
    private Client_Server_Connector _csc = null;

    private final int INIT_PROGRESS = 1;
    private final int RETURN_PROGRESS = 2;

    private static ProgressBar progressBar;
    private static TextView step2,step3,OTP;
    private static String receiver;
    // Constructors
    public Client_FileShare_Send(ProgressBar progressBar, TextView step2, TextView step3, TextView OTP, String receiver)
    {
        this.progressBar = progressBar;
        this.step2 = step2;
        this.step3 = step3;
        this.OTP = OTP;
        this.receiver = receiver;
        _csc = Client_Server_Connector.getInstance();
    }

    public static void init(ProgressBar progressBar1,TextView step22, TextView step33, TextView OTP1,String receiver1)
    {
        progressBar = progressBar1;
        step2 = step22;
        step3 = step33;
        OTP = OTP1;
        receiver = receiver1;
    }

    @Override
    protected Boolean doInBackground(String... paths) {

        isSending = true;

        Charset cs = Charset.forName("UTF-8");
        ByteBuffer[] fileNameArray = new ByteBuffer[paths.length];

        try
        {
            byte[] OTP_Packet = new byte[1024];
            OTP_Packet[0] = MAKE_OTP;
            OTP_Packet[500] = (byte) receiver.length();
            Function.frontInsertByte(550, receiver.getBytes(), OTP_Packet);
            _csc.send.setPacket(OTP_Packet).write();

            byte[] OTP_Byte = _csc.receive.setAllocate(1024).read().getByte();
            _OTP = new String(OTP_Byte).trim();

            if (_OTP.equals("0"))
            {
                //new C_Toast(null).showToast("Please Check Receiver ID.", Toast.LENGTH_LONG);
                return false;
            }

            initFiles(paths);

            for(int i = 0; i < _fileNameArray.length; i++)
            {
                fileNameArray[i] = cs.encode(_fileNameArray[i]);

                byte[] packet = new byte[1024];
                packet[0] = FILE_SHARE_RECEIVE;
                packet[1] = (byte)_fileNameArray.length;
                packet[2] = (byte)String.valueOf(_fileSizeArray[i]).getBytes().length;
                packet[3] = (byte)fileNameArray[i].limit();
                packet[500] = (byte)receiver.length();

                System.out.println(fileNameArray[i].limit());
                Function.frontInsertByte(4, String.valueOf(_fileSizeArray[i]).getBytes(), packet);
                Function.frontInsertByte(4 + String.valueOf(_fileSizeArray[i]).getBytes().length, fileNameArray[i].array(), packet);
                Function.frontInsertByte(550, receiver.getBytes(), packet);
                _csc.send.setPacket(packet).write();	// 1

                _raf = new RandomAccessFile(paths[i], "rw");
                _fileChannel = _raf.getChannel();
                PacketProcessor p = new PacketProcessor(_fileChannel, false);
                p.setAllocate(_fileSizeArray[i]);

                publishProgress(INIT_PROGRESS);
                while(!p.isAllocatorEmpty())
                {
                    _csc.send.setPacket(p.read().getByte()).write();

                    publishProgress(RETURN_PROGRESS,i);
                }
                p.close();
                System.out.println(_fileNameArray[i] + " 파일이 전송이 완료되었습니다.");
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values[0] == INIT_PROGRESS){
            _upSize =0;
            progressBar.setProgress(0);
        } else if (values[0] == RETURN_PROGRESS){
            _upSize += 1024;
            progressBar.setProgress((int) (((double)_upSize / (double) _fileSizeArray[values[1]]) * 100));
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result)
        {
            step2.setText("Upload Finish!!");
            step3.setVisibility(View.VISIBLE);
            OTP.setVisibility(View.VISIBLE);
            OTP.setText(_OTP);
        } else {
            OTP.setVisibility(View.VISIBLE);
            OTP.setText("File Upload Fail...");
            OTP.setTextColor(Color.RED);
        }
        isSending = false;
    }

    private void initFiles(String[] paths)
    {
        _fileNameArray = paths.clone();
        _fileSizeArray = new long[paths.length];

        StringTokenizer st;
        String filename = null;
        for(int i=0;i<paths.length;i++)
        {
            st = new StringTokenizer(paths[i],"/");
            while(st.hasMoreTokens())
            {
                filename = st.nextToken();
            }
            _fileNameArray[i] = _OTP + "@" + filename;

            _tempFile = new File(paths[i]);
            _fileSizeArray[i] = _tempFile.length();
        }

    }


}
