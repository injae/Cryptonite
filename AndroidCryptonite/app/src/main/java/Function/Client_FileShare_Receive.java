package Function;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import Function.PacketProcessor;
import Function.PacketRule;

import java.io.*;
import java.nio.charset.Charset;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 08 - 08
 *
 * Name : FileShare
 * Description : It can share file
 *
 * */

public class Client_FileShare_Receive extends AsyncTask<String,Integer,Integer> implements PacketRule
{
    // OTP Instance
    private String _OTP;
    private EditText otp;
    private ImageButton receiveButton;

    // File Instance
    private String _downloadFolder = null;
    private RandomAccessFile _raf = null;

    private String _downloadFlag = null;
    private String _fileName = null;
    private long _fileSize = 0;
    private PacketProcessor p = null;
    private long _downSize = 0;
    // Another Class Instance
    private Client_Server_Connector _csc = null;

    private final int UNKNOWN_ERROR = 0;
    private final int OTP_LENGTH = 1;
    private final int OTP_INVALID = 2;
    private final int SUCCESS = 3;
    private final int UPDATE_UI = 4;
    private final int UPDATE_PROGRESSBAR = 5;

    // View Instance
    private Context context;
    private TextView download;
    private ProgressBar progressBar;


    // Constructors
    public Client_FileShare_Receive(Context context, TextView download, ProgressBar progressBar, EditText otp, ImageButton receiveButton)
    {
        this.context = context;
        this.download = download;
        this.progressBar = progressBar;
        this.otp = otp;
        this.receiveButton = receiveButton;
        _csc = Client_Server_Connector.getInstance();
    }




    @Override
    protected Integer doInBackground(String... strings) {
        this._OTP = strings[1];
        Charset cs = Charset.forName("UTF-8");
        if(_OTP.length() != 6)
        {
            publishProgress(OTP_LENGTH);
            return OTP_LENGTH;
        }
        else if(_OTP.length() == 6)
        {
            try
            {

                _downloadFolder = strings[0];

                while(true)
                {
                    byte[] event = new byte[1024];
                    event[0] = FILE_SHARE_SEND;
                    _csc.send.setPacket(event).write();

                    _csc.send.setPacket(_OTP.getBytes(), 30).write();	// OTP Sending

                    _csc.receive.setAllocate(500);
                    _downloadFlag = new String(_csc.receive.read().getByte()).trim();

                    if(_downloadFlag.equals("FALSE"))
                    {
                        if (_raf == null)
                        {
                            publishProgress(OTP_INVALID);
                            return OTP_INVALID;
                        } else {
                            return SUCCESS;
                        }

                    }
                    else if(_downloadFlag.equals("TRUE"))
                    {
                        _csc.receive.setAllocate(500);
                        _fileName = cs.decode(_csc.receive.read().getByteBuf()).toString().trim();
                        System.out.println("파일 이름 : " + _fileName);

                        _csc.receive.setAllocate(500);
                        _fileSize = Long.parseLong(new String(_csc.receive.read().getByte()).trim());
                        System.out.println("파일 사이즈 : " + _fileSize);

                        System.out.println(_csc.receive.allocatorCapacity());
                        _raf = new RandomAccessFile(_downloadFolder + "/" + _fileName, "rw");

                        p = new PacketProcessor(_raf.getChannel(), false);
                        _csc.receive.setAllocate(_fileSize);

                        publishProgress(UPDATE_UI);
                        while(!_csc.receive.isAllocatorEmpty())
                        {
                            p.setPacket(_csc.receive.read().getByte()).write();
                            publishProgress(UPDATE_PROGRESSBAR);
                        }
                        System.out.println("다읽음");
                        _raf.close();
                        p.close();
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return UNKNOWN_ERROR;
            }
            catch (NullPointerException e)
            {
                System.out.println("You does not select the folder.");
                return UNKNOWN_ERROR;
            }
        }
        return UNKNOWN_ERROR;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(values[0] == OTP_LENGTH)
        {
            C_Toast t = new C_Toast(context);
            t.showToast("OTP length must be 6 letters.", Toast.LENGTH_LONG);
            download.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            otp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 6) {
                        receiveButton.setVisibility(View.VISIBLE);
                        otp.removeTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } else if (values[0] == OTP_INVALID) {
            C_Toast t = new C_Toast(context);
            t.showToast("Invalid OTP", Toast.LENGTH_LONG);
            download.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            otp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 6) {
                        receiveButton.setVisibility(View.VISIBLE);
                        otp.removeTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } else if (values[0] == UPDATE_UI) {
            download.setText(_fileName);
            progressBar.setProgress(0);
            _downSize =0;
        } else if (values[0] == UPDATE_PROGRESSBAR) {
            _downSize += 1024;
            progressBar.setProgress((int)(((double)_downSize/(double)_fileSize) * 100));
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (integer == SUCCESS){
            download.setText("Finish Download..");
            new C_Toast(context).showToast("Finish Download..",Toast.LENGTH_LONG);
        }
    }
}
