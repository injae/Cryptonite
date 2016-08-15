package Function;

import android.util.Log;


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

public class Client_FileShare_Send implements PacketRule
{
    // Instance
    private String _OTP = null;

    // About Files
    private FileChannel _fileChannel = null;
    private RandomAccessFile _raf = null;

    private String[] _fileNameArray = null;
    private int[] _fileNameSize = null;

    private File _tempFile = null;
    private long[] _fileSizeArray = null;

    // Another Class Instance
    private Client_Server_Connector _csc = null;

    // Constructors
    public Client_FileShare_Send()
    {
        _csc = Client_Server_Connector.getInstance();
    }



    public void sendFile(String[] paths)	// when you click send button
    {

        Charset cs = Charset.forName("UTF-8");
        ByteBuffer[] fileNameArray = new ByteBuffer[paths.length];

        try
        {
            byte[] garbage = new byte[1024];

            byte[] OTP_Packet = new byte[1024];
            OTP_Packet[0] = MAKE_OTP;
            _csc.send.setPacket(OTP_Packet).write();
            _csc.send.setPacket(garbage).write();

            byte[] OTP_Byte = _csc.receive.read().getByte();
            _OTP = new String(OTP_Byte).trim();

            initFiles(paths);

            for(int i = 0; i < _fileNameArray.length; i++)
            {
                fileNameArray[i] = cs.encode(_fileNameArray[i]);

                byte[] packet = new byte[1024];
                packet[0] = FILE_SHARE_RECEIVE;
                packet[1] = (byte)_fileNameArray.length;
                packet[2] = (byte)String.valueOf(_fileSizeArray[i]).getBytes().length;
                packet[3] = (byte)fileNameArray[i].limit();
                System.out.println(fileNameArray[i].limit());
                Function.frontInsertByte(4, String.valueOf(_fileSizeArray[i]).getBytes(), packet);
                Function.frontInsertByte(4 + String.valueOf(_fileSizeArray[i]).getBytes().length, fileNameArray[i].array(), packet);
                _csc.send.setPacket(packet).write();	// 1

                _raf = new RandomAccessFile(paths[i], "rw");
                _fileChannel = _raf.getChannel();
                PacketProcessor p = new PacketProcessor(_fileChannel, false);
                p.setAllocate(_fileSizeArray[i]);

                while(!p.isAllocatorEmpty())
                {
                    _csc.send.setPacket(p.read().getByte()).write();
                }
                p.close();
                System.out.println(_fileNameArray[i] + " 파일이 전송이 완료되었습니다.");
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.exit(1);
            e.printStackTrace();
        }
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
            Log.d("send",String.valueOf(_fileNameArray[i]));
        }
    }
}
