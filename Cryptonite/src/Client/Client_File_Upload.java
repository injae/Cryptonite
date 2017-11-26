package Client;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JWindow;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.PacketRule;
import Function.Function;
import Function.PacketProcessor;

public class Client_File_Upload extends Thread implements PacketRule
{
	// Instance
	private String[] _fileNameArray;
	private String[] _filePathArray;
	private long[] _fileSizeArray;
	
	private RandomAccessFile _raf = null;
	private FileChannel _fileChannel = null;
	
	private String _gpCode;
	private boolean _check;
	private int _mod = 0;
	private String password = null;
	private String pbk = null;
	
	// Another Class
	private Client_Server_Connector _csc = null;
	private Client_FileSelector _cfs = null;
	private Client_Progressbar _cpb = null;
	
	private Crypto _crypto = null;
	private KeyReposit _reposit = null;
	private SecretKey key = null;
	
	// Constructors
	public Client_File_Upload()
	{
		_reposit = KeyReposit.getInstance();
		_csc = Client_Server_Connector.getInstance();
		_cfs = new Client_FileSelector();
		_cpb = new Client_Progressbar(3);
	}
	
	// Methods
	public void run()
	{
		try 
		{	
			_check = false;
			_mod = 0;
			
			EncMod em = new EncMod();
			em.UI_ON();
			while(!em.isfinished)
			{
				try 
				{
					Thread.sleep(1);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
			_mod = em.getMod();
			if (_mod == -1) {return;}
			if(!fileSelection()) { return; }
			
			_cpb.UI_ON();
			
			if (_mod == 0)
			{
				key = new Client_Get_Group_Key().running(_gpCode,0);
				_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, key));
			}
			else
			{
				pbk = getPBK(password, Integer.parseInt(_gpCode.substring(1)));
				key = new SecretKeySpec(pbk.concat("0000").getBytes(), "AES");
				_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, key));
				System.out.println(pbk.length());
				System.out.println(_gpCode);
			}
			
			
			
			for(int i = 0; i < _fileNameArray.length; i++)
			{
				byte[] event = new byte[1024];
				event[0] = FILE_UPLOAD;
				event[1] = (byte)_fileNameArray[i].getBytes().length;
				
				if (_mod == 0)
					event[2] = (byte)String.valueOf(_fileSizeArray[i]).getBytes().length;
				else
					event[2] = (byte)String.valueOf(_fileSizeArray[i]+64).getBytes().length;

				
				Function.frontInsertByte(3, _fileNameArray[i].getBytes(), event);
				
				if (_mod == 0)
					Function.frontInsertByte(3 + _fileNameArray[i].getBytes().length, String.valueOf(_fileSizeArray[i]).getBytes(), event);
				else
					Function.frontInsertByte(3 + _fileNameArray[i].getBytes().length, String.valueOf(_fileSizeArray[i]+64).getBytes(), event);

				Function.frontInsertByte(800, _gpCode.getBytes(), event);
				
				_csc.send.setPacket(event).write();
				
				_raf = new RandomAccessFile(_filePathArray[i], "rw");
				_fileChannel = _raf.getChannel();
				PacketProcessor p = new PacketProcessor(_fileChannel, false);
				
				p.setAllocate(_fileSizeArray[i]);
				System.out.println(_fileSizeArray[i]);
				
			
				if (_mod == 1)
				{
					_csc.send.setAllocate(64);
					_csc.send.setAllocate(_fileSizeArray[i]);
					_csc.send.setPacket(SHA(pbk.concat("0000")).getBytes(),64).write();
				}
				else
					_csc.send.setAllocate(_fileSizeArray[i]);
				
				while(!p.isAllocatorEmpty())
				{
					_crypto.init(Crypto_Factory.create("AES256", Cipher.ENCRYPT_MODE, key));
					_csc.send.setPacket(_crypto.endecription(p.read().getByte())).write();
				//	_csc.send.setPacket(p.read().getByte()).write();
				}
				p.close();

				System.out.println(_fileNameArray[i] + " was sent completely.");
				_check = true;
				_cfs.dispose();
			}
			_cpb.UI_OFF();
			showMessage("Notification", "All Files were sent completely.");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void setGpCode(String gpCode)
	{
		_gpCode = gpCode;
	}
	
	public boolean getCheck()
	{
		return _check;
	}
	
	private boolean fileSelection()
	{
		Charset cs = Charset.forName("UTF-8");
		
		_cfs.fileFinderON();
		while(!_cfs.getSelectionFinish())
		{
			try 
			{
				Thread.sleep(1);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		_fileNameArray = _cfs.getFileNames();
		_filePathArray = _cfs.getFilePaths();
		
		if(_fileNameArray == null) { return false; }
		
		for(int i = 0; i < _fileNameArray.length; i++)
		{
			if (_mod == 0)
				_fileNameArray[i] += ".cnec";
			else 
				_fileNameArray[i] += ".cnmc";
		}
		
		_fileSizeArray = new long[_filePathArray.length];
		for(int i = 0; i < _filePathArray.length; i++)
		{
			File temp = new File(_filePathArray[i]);
			_fileSizeArray[i] = temp.length();
		}

		return true;
	}
	
	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,24);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	class EncMod  implements PacketRule
	{
		// Instance
		private JWindow _window;
		private JLabel _background;
		private JButton _default,pbk;
		private JButton _close;
		private JLayeredPane _layeredPane;
		private boolean isfinished = false;
		private int mod = 0;
		
		// Constructors
		public EncMod()
		{
			_layeredPane = new JLayeredPane();
			_window = new JWindow();
			
			_background = new JLabel(new ImageIcon("img/Setting.png"));
			_background.setBounds(0, 0, 400, 224);
			_background.setVisible(true);
			
			_close = new JButton(new ImageIcon("img/SClose.png"));
			_close.setRolloverIcon(new ImageIcon("img/SCloseR.png"));
			_close.setBounds(306, 160, 90, 60);
			_close.setVisible(true);
			_close.setBorderPainted(false);
	 	    _close.setFocusPainted(false);
			_close.addActionListener(new ActionListener() 
	 	    {
	 	       	public void actionPerformed(ActionEvent arg0) 
	 	       	{
	 	       		mod = -1;
	 	       		isfinished = true;
	 	       		UI_OFF();
	 	       	}
	 	    });
			
			_layeredPane.add(_close);
			
			
			_default = new JButton(new ImageIcon("img/file_upload_default.png"));
			_default.setRolloverIcon(new ImageIcon("img/file_upload_default_R.png"));
			_default.setBounds(32, 32, 128, 128);
			_default.setVisible(true);
			_default.setBorderPainted(false);
			_default.setFocusPainted(false);
			_default.setContentAreaFilled(false);
			_default.addActionListener(new ActionListener() 
	 	    {
	 	       	public void actionPerformed(ActionEvent arg0) 
	 	       	{
	 	       		mod = 0;
	 	       		isfinished = true;
	 	       		UI_OFF();
	 	       	}
	 	    });
			
			pbk = new JButton(new ImageIcon("img/file_upload_pbk.png"));
			pbk.setRolloverIcon(new ImageIcon("img/file_upload_pbk_R.png"));
			pbk.setBounds(32+128+32, 32, 128, 128);
			pbk.setVisible(true);
			pbk.setBorderPainted(false);
			pbk.setFocusPainted(false);
			pbk.setContentAreaFilled(false);
			pbk.addActionListener(new ActionListener() 
	 	    {
	 	       	public void actionPerformed(ActionEvent arg0) 
	 	       	{
	 	       		password = getPassword("");
	 	       		if (password == null)
	 	       		{
	 	       			mod = -1;
	 	       			JOptionPane.showMessageDialog(null, "Upload Canceled!!","Cancel",JOptionPane.OK_OPTION);
	 	       		}
	 	       		else
	 	       			mod = 1;
	 	       		isfinished = true;
	 	       		UI_OFF();
	 	       	}
	 	    });
			
			_layeredPane.add(_default);
			_layeredPane.add(pbk);
			
			_layeredPane.add(_background);
			
			_window.getContentPane().add(_layeredPane);
			_window.setSize(400, 224);
			_window.setLocationRelativeTo(null);
			_window.setAlwaysOnTop(false);
		}
		
		public void UI_ON()
		{
			_window.setVisible(true);
		}
		
		private boolean isFinished()
		{
			return isfinished;
		}
		
		public int getMod()
		{
			return mod;
		}
		
		private void UI_OFF()
		{
			_window.dispose();
		}
		
	}
	
	
	private String getPBK (String password, int gpCode)
	{
		byte[] op = new byte[1024];
		String salt = null;
		int iteration = 0;
		byte size =1;
		try {
			op[0]=GET_PBKDF2_GROUP;
			op[1]=size;
			op[2]=(byte) gpCode;
			_csc.send.setPacket(op).write();
			
			salt = new String(_csc.receive.setAllocate(32).read().getByte());
			iteration = byteArrayToInt(_csc.receive.setAllocate(4).read().getByte());
			
			System.out.println("Group: " +salt +"  " + iteration);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pbkdf2(password,salt,iteration);
		
	}
	
	public  int byteArrayToInt(byte bytes[]) {
		return ((((int)bytes[0] & 0xff) << 24) |
				(((int)bytes[1] & 0xff) << 16) |
				(((int)bytes[2] & 0xff) << 8) |
				(((int)bytes[3] & 0xff)));
	} 
    public String pbkdf2(String password, String salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 20*8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return new String(Base64.getEncoder().encode(hash));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("error on pbkdf2", e);
        }
    }
    
    public String SHA(String str)
    {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e2) {
			e2.printStackTrace();
		} 
        md.update(str.getBytes()); 
        byte byteData[] = md.digest();
        
        StringBuffer sb = new StringBuffer(); 
        for(int i=0; i<byteData.length; i++) {
            sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    
	private String getPassword(String name) {
		String password = "";
		JPasswordField passwordField = new JPasswordField();
		Object[] obj = {"Input "+ name + " Password\nWarning!!\nIf you forget your password, you will not be able to decrypt the file.",passwordField};
		if (JOptionPane.showOptionDialog(null, obj, "Password", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
			password = new String(passwordField.getPassword());
		}
		return password;
	}
}
