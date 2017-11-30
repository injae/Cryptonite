package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.Stack;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import Client.Client_Group_Main.RecoveryButton;
import Crypto.KeyReposit;
import Function.Function;
import Function.PacketRule;

public class Client_FileRecovery extends JFrame implements PacketRule
{	
	private BufferedImage _img = null;
	private final int MAX_BTN = 18;
	private final int COLUMN = 3;
	private final int ROW = 6;
	
	private Client_File_ListReceiver _cfl = null;
	
	private ArrayList<String> _fileList;
	
	private JButton _Download;
	private JButton _Refresh;
	private JButton _Right;
	private JButton _Left;
	private JButton _Back;
	
	private int _nowPage = 0;
	private int _page;
	
	private Container container;
	private JLayeredPane layeredPane = new JLayeredPane();

	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(_img, 0, 0, null);
            g.setColor(Color.BLACK);
			g.setFont(_precondition_font);
			g.drawString((_nowPage + 1) + "/" + _page, 307, 406);
        }
   }
	private MyPanel panel = new MyPanel();

	private Font fontbt = new Font("SansSerif", Font.BOLD,10);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,15);
	
	
	public class RecoveryButton
	{
		public RecoveryButton(String fullpath)
		{
			fullPath = fullpath;
			
			StringTokenizer st = new StringTokenizer(fullpath, "\\");
			
			while(st.hasMoreTokens())
			{
				fileName = st.nextToken();
			}
			
			isClick = false;
			
			st = new StringTokenizer(fileName, ".");
			if(st.countTokens() == 1) { isDir = true; }
			else 					  { isDir = false;}
		}
		
		private boolean isClick;
		public void click()
		{
			if(isClick)  { isClick = false; }
			else 		 { isClick = true;  }
		}
		public String noExtensionName() { return fileName.substring(0, fileName.length() - 5); }
		public String ExtensionName() { return fileName;}
		public String Extension() {return fileName.substring(fileName.length()-4,fileName.length());}
		
		public boolean isDir;
		public JButton button;
		public String fullPath;
		public String fileName;
	}
	private ArrayList<RecoveryButton> _btnList;
	private String _downloadPath = "NULL";
	private Stack<ArrayList<RecoveryButton>> _undo;
	
	
	public Client_FileRecovery(ArrayList<String> fileList) 
	{
		_btnList = new ArrayList<RecoveryButton>();
		_undo = new Stack<ArrayList<RecoveryButton>>();
		_fileList=fileList;
		while(!fileList.isEmpty())
		{
			_btnList.add(new RecoveryButton(fileList.remove(0)));
		}
		pageCount();
		
		try
		{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}	
		
		container=getContentPane();
		container.setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(0,0,656,480);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
        layeredPane.setBounds(0, 0, 656, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            _img = ImageIO.read(new File("gui/File Recovery_BG.png"));
        }
        catch (IOException e)
        {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
        
        panel.setBounds(0, 0, 656, 480);
		
        allocator();
		makeBtn();
		page();
		basic();
        setVisible(true);
	}

	private void pageCount()
	{
		if((_btnList.size() % MAX_BTN) == 0)
		{
			_page = (_btnList.size() / MAX_BTN);
			if(_btnList.isEmpty()) { _page  = 1;}
		}
		else
		{
			_page = (_btnList.size() / MAX_BTN) + 1;
		}
		_nowPage = 0;
	}
	
	private void makeBtn() 
	{
		try
		{
			for(int k = 0; k < _page; k++)
			{
				int x = 10 , y = 0;
				for(int j = 0; j < COLUMN; j++)
				{
					x = 15;
					for(int i = 0; i < ROW; i++)
					{		
						if(_btnList.get(k * MAX_BTN + (j * ROW) + i).isDir)
						{
							makeFolder(k * MAX_BTN + (j * ROW) + i, x, y);
						}
						else
						{
							makeFile(k * MAX_BTN + (j * ROW) + i, x, y);
						}
						if(_btnList.size() - 1 <= (k * MAX_BTN + (j * ROW) + i)) { return; }
						x += 100;
					}
					y += 100;
				}
			}
		}
		catch(IndexOutOfBoundsException e1)
		{
		}
	}
	
	private void makeFile(int index, int x, int y)
	{
		JButton btn = null;
		if(_btnList.get(index).button!=null) { return; }
		if (_btnList.get(index).fileName.substring(_btnList.get(index).fileName.length()-4, _btnList.get(index).fileName.length()).equals("cnmc"))
			btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/file_cnmc.png"));
		else 
			btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/file.png"));
		btn.setFont(fontbt);
		btn.setToolTipText(_btnList.get(index).ExtensionName());
		if (_btnList.get(index).fileName.substring(_btnList.get(index).fileName.length()-4, _btnList.get(index).fileName.length()).equals("cnmc"))
			btn.setPressedIcon(new ImageIcon("gui/file_cnmc.png"));
		else
			btn.setPressedIcon(new ImageIcon("gui/file.png"));
		btn.setBounds((7+x),(80+y),80,120);
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setVerticalAlignment(SwingConstants.TOP);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				_btnList.get(index).click();
				layeredPane.removeAll();
				makeBtn();
				page();
				basic();
				layeredPane.repaint();

				if(!_btnList.get(index).isClick)
				{	
					if (_btnList.get(index).fileName.substring(_btnList.get(index).fileName.length()-4, _btnList.get(index).fileName.length()).equals("cnmc"))
						_btnList.get(index).button.setIcon(new ImageIcon("gui/file_cnmc.png"));
					else 
						_btnList.get(index).button.setIcon(new ImageIcon("gui/file.png"));
				}
				else
				{
					if (_btnList.get(index).fileName.substring(_btnList.get(index).fileName.length()-4, _btnList.get(index).fileName.length()).equals("cnmc"))
						_btnList.get(index).button.setIcon(new ImageIcon("gui/file_cnmc_check.png"));
					else
						_btnList.get(index).button.setIcon(new ImageIcon("gui/file_check.png"));
				}
			}
		});
		_btnList.get(index).button = btn;
	}
	
	private void makeFolder(int index, int x, int y)
	{
		if(_btnList.get(index).button!=null){return;}
		JButton btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/_folder.png"));
		btn.setPressedIcon(new ImageIcon("gui/_folderR.png"));
		btn.setToolTipText(_btnList.get(index).fileName);
		btn.setFont(fontbt);
		btn.setBounds((7+x),(80+y),75,110);
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setVerticalAlignment(SwingConstants.TOP);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				for(int i = 0; i < _btnList.size(); i++)
				{
					if(_btnList.get(i).isClick)
					{
						_btnList.get(i).isClick=false;
						_btnList.get(i).button.setIcon(new ImageIcon("gui/file.png"));
					}
				}
				_undo.push(_btnList);
				String fullPath = _btnList.get(index).fullPath + "\\";
				
				ArrayList<String> files = new Client_Folder_List().running(_btnList.get(index).fullPath).getFileList();
				_btnList = new ArrayList<RecoveryButton>();
				
				for(int i =0; i < files.size(); i++)
				{
					_btnList.add(new RecoveryButton(fullPath + files.get(i)));
				}
				pageCount();
				layeredPane.removeAll();
				makeBtn();
				page();
				basic();
				layeredPane.repaint();
				
			}
		});
		_btnList.get(index).button = btn;
	}
	
	private void basic()
	{
		layeredPane.add(_Left);
		layeredPane.add(_Right);
		layeredPane.add(_Download);
        layeredPane.add(_Back);  
        layeredPane.add(_Refresh);
        layeredPane.add(panel);
        container.add(layeredPane);
	}
	
	private void page()
	{
		try
		{
			int k = _nowPage;
			for(int j = 0; j < COLUMN; j++)
			{
				for(int i = 0; i < ROW; i++)
				{		
					layeredPane.add(_btnList.get(k * MAX_BTN + (j * ROW) + i).button);
					if(_btnList.size() - 1 <= (k * MAX_BTN + (j * ROW) + i)) { return; }
				}
			}
		}
		catch(IndexOutOfBoundsException e1)
		{
		}
	}
	
	private void selectFolder()
	{
		Client_FolderSelector cfs = new Client_FolderSelector();
		cfs.folderSelectorON();
		 while(!cfs.getSelectionEnd())
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
		 _downloadPath = cfs.getSelectedPath();
	}
	
	private void allocator()
	{
		 _Download = new JButton(new ImageIcon("gui/download.png"));	
		 _Download.setRolloverIcon(new ImageIcon("gui/downloadR.png"));
		 _Download.setBounds(490, 10, 70,70);
		 _Download.setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
		 _Download.setVerticalAlignment    ( SwingConstants.TOP ) ;
		 _Download.setHorizontalTextPosition( SwingConstants.CENTER ) ;
		 _Download.setBorderPainted(false);
		 _Download.setFocusPainted(false);
		 _Download.setContentAreaFilled(false);
		 _Download.addActionListener(new ActionListener() 
		 {
			 public void actionPerformed(ActionEvent e) 
			 {
				 selectFolder();
				 if(_downloadPath != null)
				 {
					 SecretKey key = null;
					 for(int i =0; i < _btnList.size(); i++)
					 {
						 if(_btnList.get(i).isClick)
						 {
							 if (_btnList.get(i).Extension().equals("cnmc"))
							 {
								 String temp = getPassword(_btnList.get(i).noExtensionName());
								 if (temp==null)
								 {
									 JOptionPane.showConfirmDialog(null, _btnList.get(i).noExtensionName() + " download Canceled!");
									 continue;
								 }
								 String pbk = getPBK(temp);
								 
								 String sha = SHA(pbk.concat("0000"));
								 
								 if (!sha.equals(getFileSHA(_btnList.get(i).fullPath)))
								 {
									 JOptionPane.showMessageDialog(null, "The password is incorrect.\n"+_btnList.get(i).fileName+" download canceled!!","Error",JOptionPane.OK_OPTION);
									 continue;
								 }
								 
								 key = new SecretKeySpec(pbk.concat("0000").getBytes(),"AES");
							 }
							 else
								 key = KeyReposit.getInstance().get_aesKey();
							 new Client_File_Download().requestFile(_btnList.get(i).fullPath, _downloadPath + "\\" + _btnList.get(i).fileName, key);
						 }
					 } 
				 }
			 }
		 });	
				
		 
		 _Left = new JButton(new ImageIcon("gui/Left.png"));
		 _Left.setRolloverIcon(new ImageIcon("gui/LeftR.png"));
		 _Left.setBounds(250, 380, 80, 40);
		 _Left.setFocusPainted(false);
		 _Left.setContentAreaFilled(false);
		 _Left.setBorderPainted(false);
		 _Left.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				 _nowPage -= 1;
				 if(_nowPage < 0)
				 {
					 _nowPage += 1;
					 showMessage("error", "Here is the first page!");
				 }
				 else
				 {   	
					 layeredPane.removeAll();

					 page();
					 basic();
					 repaint();
				 }
			 }
		 });
		 
		 _Right = new JButton(new ImageIcon("gui/Right.png"));
		 _Right.setRolloverIcon(new ImageIcon("gui/RightR.png"));
		 _Right.setBounds(305, 380, 80, 40);
		 _Right.setFocusPainted(false);
		 _Right.setContentAreaFilled(false);
		 _Right.setBorderPainted(false);
		 _Right.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				 _nowPage += 1;
				 if(_nowPage + 1 > _page)
				 {
					 _nowPage -= 1;
					 showMessage("error", "Here is the last page!");
				 }
				 else
				 { 
					 layeredPane.removeAll();
			         		
					 page();
					 basic();
					 repaint();
				 } 
			 }
		 });

		 _Back = new JButton(new ImageIcon("gui/back_icon.png"));
		 _Back.setRolloverIcon(new ImageIcon("gui/back_iconR.png"));
		 _Back.setBounds(550, 10, 80, 40);
		 _Back.setFocusPainted(false);
		 _Back.setContentAreaFilled(false);
		 _Back.setBorderPainted(false);
		 _Back.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				 if(_undo.isEmpty()) return;
				_btnList=_undo.pop();
				
				layeredPane.removeAll();
				makeBtn();
				pageCount();
				page();
				basic();
				repaint();
			 }
		 });
		 
		 _Refresh = new JButton(new ImageIcon("img/refresh_icon2.png"));
		 _Refresh.setRolloverIcon(new ImageIcon("img/refresh_icon2R.png"));
		 _Refresh.setBounds(155, 11, 80, 40);
		 _Refresh.setFocusPainted(false);
		 _Refresh.setContentAreaFilled(false);
		 _Refresh.setBorderPainted(false);
		 _Refresh.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				/* if(_undo.isEmpty()) return;
				_btnList=_undo.pop();*/
				 _fileList.clear();
				 _btnList.clear();
				 _cfl = new Client_File_ListReceiver();
				 _cfl.running((byte)2, null);
				 _fileList=_cfl.getFileList();
				while(!_fileList.isEmpty())
				{
					_btnList.add(new RecoveryButton(_fileList.remove(0)));
				}
				pageCount();
				 
				layeredPane.removeAll();
				makeBtn();
				pageCount();
				page();
				basic();
				repaint();
			 }
		 });
		 
	}
	
	private String getFileSHA(String path) {
		Client_Server_Connector css = Client_Server_Connector.getInstance();
		
		byte[] op = new byte[1024];
		byte size =1;
		String sha = null;
		try {
			op[0]=GET_FILE_SHA_HEADER;
			op[1]=size;
			op[2] = (byte)String.valueOf(path).getBytes().length;
			Function.frontInsertByte(3, String.valueOf(path).getBytes(), op);
			
			css.send.setPacket(op).write();
			
			
			sha = new String(css.receive.setAllocate(64).read().getByte());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sha;
	}

	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,24);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private String getPBK (String password)
	{
		Client_Server_Connector _css = Client_Server_Connector.getInstance();
		byte[] op = new byte[1024];
		String salt = null;
		int iteration = 0;
		byte size =1;
		try {
			op[0]=GET_PBKDF2;
			op[1]=size;
			_css.send.setPacket(op).write();
			
			salt = new String(_css.receive.setAllocate(32).read().getByte());
			iteration = byteArrayToInt(_css.receive.setAllocate(4).read().getByte());
			
			System.out.println(salt +"  " + iteration);
			
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
		String password = null;
		JPasswordField passwordField = new JPasswordField();
		Object[] obj = {"Input "+name+" Password",passwordField};
		if (JOptionPane.showOptionDialog(null, obj, "Password", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
			password = new String(passwordField.getPassword());
		}
		return password;
	}
}