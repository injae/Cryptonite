package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Function.PacketRule;
import Server.Server_DataBase;

	public class Client_SignUp extends JFrame implements PacketRule //Create new account
	//Create new page
	
	{											
		BufferedImage _img=null;
		SHA_256 _sha = null;
		
		public static void main(String[] args){
			new Client_SignUp();
		}
		private boolean _checklength=true;
		private boolean _checkID = true;
		private boolean _checkSame=false;
		private boolean _checkPassword=false;
		public static boolean _goSignUP = false;
		public static boolean _goFolderScan = false;
		
		public static boolean getGoSignUP()
		{		   
			return _goSignUP;

		}

		public static boolean getgoFoldercan()
		{
			return _goFolderScan;
		}	
		
		private void showMessage(String _title, String _message) 
		{
			JOptionPane.showMessageDialog(null, _message, _title, JOptionPane.INFORMATION_MESSAGE);
		}

		private String serverIP=null;//input serverip
		//private int serverPort = 10000;
		private SocketChannel socket=null;

		
		JTextField _nameField;
		JTextField _idField;
		JTextField _emailField;
		JPasswordField _passwdField;
		JPasswordField _passwdCorrectField;
		
		
		private String _name="name";
		private String _id="";
		private String _email="email";
		private String _password="password";
		private String _passwordCorrect="passwordCorrect";
		
		Client_Server_Connector _csc;

		JButton _same;
		JButton _ok;
		JButton _cancel;

		 Font _font = new Font ("SansSerif", Font.BOLD,17);
		 Font _precondition_font = new Font ("SansSerif", Font.ITALIC,16);
	
		Client_Server_Connector _css;

		public Client_SignUp(){
			                                              
			try {
				_csc=Client_Server_Connector.getInstance(4444);
			} catch (InterruptedException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
			setTitle("CRYPTONITE");
			setBounds(710,200,482,725);//Input value
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			setBackground(Color.BLACK);
			setLocationRelativeTo(null);
			try
			{
				_img = ImageIO.read(new File("gui/signup_main.png"));//Input MainImage
				
			}catch(IOException e){
				System.out.println("No Image.");
				System.exit(0);
			}

			JLayeredPane _layeredpane =new JLayeredPane();
			_layeredpane.setBounds(0, 0, 510, 660);//input value
			_layeredpane.setLayout(null);

			Mypanel _panel = new Mypanel();
			_panel.setBounds(0,0,490,655);//input value  

			_nameField = new JTextField();
			_nameField.setBounds(170, 200, 160, 21);//input value
			_nameField.setFont(_font);
			_nameField.setBorder(BorderFactory.createEmptyBorder());
			_nameField.setForeground(Color.BLACK);
			_nameField.setOpaque(false); 

			_nameField.addKeyListener(new KeyListener()
			{
				@Override
				public void keyPressed(KeyEvent _e) { }
			
				@Override
				public void keyReleased(KeyEvent _e) 
				{
					_name = _nameField.getText();
				}


				@Override
				public void keyTyped(KeyEvent _e)
				{}

			});
			_layeredpane.add(_nameField);

			_idField = new JTextField();
			_idField.setBounds(170, 257, 160, 20);//input value
			_idField.setFont(_font);
			_idField.setForeground(Color.BLACK);
			_idField.setBorder(BorderFactory.createEmptyBorder());
			_idField.setOpaque(false);

			_idField.addKeyListener(new KeyListener()
			{
				@Override
				public void keyPressed(KeyEvent _e)  { }

				@Override
				public void keyReleased(KeyEvent _e) 
				{
					_id = _idField.getText();
					_checkSame=false;
				}

				@Override
				public void keyTyped(KeyEvent _e) { }
			}); 
			_layeredpane.add(_idField);
			
			_passwdField = new JPasswordField();
			_passwdField.setBounds(170, 313, 160, 20);//input value
			_passwdField.setFont(_font);
			_passwdField.setEchoChar('●');
			_passwdField.setBorder(BorderFactory.createEmptyBorder());
			_passwdField.setForeground(Color.BLACK);
			_passwdField.setOpaque(false);
			_passwdField.addKeyListener(new KeyListener()
		{
				@Override
				public void keyPressed(KeyEvent _e) { }
				
				@Override
				public void keyReleased(KeyEvent _e) 
				{
					_password = new String(_passwdField.getPassword());
				
			}

				@Override
				public void keyTyped(KeyEvent _e) { }
		});
			_layeredpane.add(_passwdField);

			_passwdCorrectField = new JPasswordField(15);
			_passwdCorrectField.setBounds(170, 370, 160, 20);//input value
			_passwdCorrectField.setFont(_font);
			_passwdCorrectField.setEchoChar('●');
			_passwdCorrectField.setBorder(BorderFactory.createEmptyBorder());
			_passwdCorrectField.setForeground(Color.BLACK);
			_passwdCorrectField.setOpaque(false);
			_passwdCorrectField.addKeyListener(new KeyListener()
		{
				@Override
				public void keyPressed(KeyEvent _e) { }
				
				@Override
				public void keyReleased(KeyEvent _e) 
			{
				_passwordCorrect = new String( _passwdCorrectField.getPassword());

				if(_passwordCorrect.equals(_password))
				{
					_checkPassword = true;
				}
				else if( _passwdCorrectField.equals(_password) == false ||  _passwdCorrectField.equals(null) == false)
				{
					_checkPassword = false;
				}

			}

			@Override
			public void keyTyped(KeyEvent _e) { }

		});
		_layeredpane.add( _passwdCorrectField);

		_emailField = new JTextField();
		_emailField.setBounds(170, 424, 140, 20);//input value
		_emailField.setBorder(BorderFactory.createEmptyBorder());
		_emailField.setForeground(Color.BLACK);
		_emailField.setOpaque(false);
		_emailField.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent _e) { }

			@Override
			public void keyReleased(KeyEvent _e)
			{
				_email = _emailField.getText();
			}

			@Override
			public void keyTyped(KeyEvent _e) { }

		});
		_layeredpane.add(_emailField);

		_same = new JButton(new ImageIcon("gui/check_bt.png"));//Input IconImage
		_same.setPressedIcon(new ImageIcon("img/_check_bt.png"));
		_same.setBounds(320, 250, 80, 38);//input value
		_same.setBorderPainted(false);
		_same.setFocusPainted(false);
		_same.setContentAreaFilled(false);
		_same.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				byte size=2;
				_csc.configurePacket("id");
				
				byte[] event=new byte[3];
				event[0] =SIGN_UP;
				event[1]=1;
				event[2]=size;
			
				_csc.setPacket("id", event);
				_csc.setPacket("id", _id.getBytes());
				
				_csc.send("id");
				try 
				{
					byte[] _Checkid;
					
						_Checkid = _csc.receiveByteArray();
						System.out.println(_Checkid[0]);
						/*if(_id.length()<3)
						{ 
							_checkID=false;
						}*/
						if(_id.equals(""))
						{ 
							_checkID=false;
						}
						System.out.println(_id);
						System.out.println(_checkID);
						switch(_Checkid[0])
						{
						case 1 :
							_checkSame=true;
							
							if(_checkID)
							{ 
								if(_id.length()<3){showMessage("ERROR", "Please insert at least 3 characters");}
								else{showMessage("ID", "This ID can be used."); }
								
							}
							else
							{  
								showMessage("ERROR", "Please insert ID");
								_checkID=true;
								/*	if(_id.length()==2&&(_id.equals("id"))){ showMessage("ERROR", "Please insert ID");}
								else { showMessage("ERROR", "Please insert at least 3 characters");}*/
							}
							break;
						case 2:
							_checkSame=false;
							showMessage("ERROR", "This ID is already in use. Please use another ID.");
							break;
					}
				} catch (IOException e) {
					// TODO 자동 생성된 catch 블록
					e.printStackTrace();
				}
			}
		});
		_layeredpane.add(_same);

		_cancel = new JButton(new ImageIcon("gui/cancel_bt.png"));//Input IconImage
/*		_cancel.setRolloverIcon(new ImageIcon(""));*/
		_cancel.setPressedIcon(new ImageIcon("img/_cancel_hv.png"));

		_cancel.setBounds(190, 560, 90, 50);//input value
		_cancel.setBorder(BorderFactory.createEmptyBorder());

		_cancel.setBorderPainted(false);
		_cancel.setFocusPainted(false);
		_cancel.setContentAreaFilled(false);

		_cancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				dispose();
			}

		});
		_layeredpane.add(_cancel);

		_ok = new JButton(new ImageIcon("gui/signup_bt.png"));//Input IconImage
		/*_ok.setRolloverIcon(new ImageIcon(""));*/
		_ok.setPressedIcon(new ImageIcon("img/signup_bt_hv.png"));

		_ok.setBounds(30, 500, 400, 50);//input value
		_ok.setBorderPainted(false);
		_ok.setFocusPainted(false);
		_ok.setContentAreaFilled(false);

		_ok.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent _arg0)
			{
				if(_password.length()<5||_passwordCorrect.length()<5){
					_checklength=false;
				}
				else if(_password.length()>=5&&_passwordCorrect.length()>=5){
					_checklength=true;
				}
				if(!_name.equals("name") && !_id.equals("")&& !_password.equals("password") &&!_passwordCorrect.equals("passwordCorrect") 
						&& !_email.equals("email") &&_checkSame&&_checkID)
				{
					if(_checkPassword&&_checklength)
					{
						 _goSignUP=true;
						
					}
					else if(_checkPassword&&!_checklength)
					{
						showMessage("ERROR", "Please check the requirements of the password.");
					}
					else if(!_checkPassword&&_checklength)
					{
						showMessage("ERROR", "Passcodes did not match.");
					}
					else if (!_checkPassword&&!_checklength){
						showMessage("ERROR","Please check the requirements of the password. andPasscodes did not match.");
					}
				}
				else
				{
					showMessage("ERROR", "Please fill in all the blanks.");
				}
				
				if(_goSignUP){
					
					showMessage("WELCOME!", "WELCOME TO CRYPTONITE!.");

					//userKeyGenerator _ukg = new userKeyGenerator();
					_sha = new SHA_256(_name,_id,_password,_email/*,_ukg.genEncAesKey(_password), _ukg.getSalt(), _ukg.getIterationCount()*/);

					dispose();
				}
				else{
					if(!_checkSame&&_checkID)
					{
						showMessage("ERROR", "Check whether the duplicates ID");
					}
					else if(_checkSame&&!_checkID)
					{
						showMessage("ERROR", "Check ID's precondition(?)");
					}
					else{showMessage("ERROR", "Check whether the duplicates ID and ID's precondition");}
				/*	else if(_checkSame)
					{
						showMessage("ERROR", "Did not enter the all items. Or ID are duplicated. Or Passcodes did not match.");
					}*/
				}
			}
		});

		_layeredpane.add(_ok);
		_layeredpane.add(_panel);
		getContentPane().add(_layeredpane);


		setVisible(true);
	}
	class Mypanel extends JPanel
	{
		public void paint(Graphics _g)
		{
			_g.drawImage(_img,0,0,null);
			_g.setColor(Color.BLACK);
			_g.setFont(_precondition_font);
			_g.drawString("->Please insert at least 5 characters.", 100, 355);
			_g.drawString("->Please insert at least 3 characters.", 100, 300);
		}
	}
}
class SHA_256 implements PacketRule

	{	
		MessageDigest _messageDigest = null;
		Client_Server_Connector _css;

		private String _name;
		private String _id;
		private String _password;
		private String _email;

		private byte[] _AES_Key;
		private byte[] _salt;
		private int _iteration;

		private byte[] _temp_name;
		private byte[] _temp_pwd;
		private byte[] _temp_email;
		
		private byte[] _temp_data;
		private byte _size;

		public SHA_256(String _name, String _id, String _password, String _email/*, byte[] _AES_Key, byte[] _salt, int _iteration*/)
		{	
			try {
				_css=Client_Server_Connector.getInstance(4444);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			this._name=_name;
			this._id = _id;
			this._password = _password;
			this._email = _email;
			/*this._AES_Key = _AES_Key;
		 	this._salt = _salt;
		 	this._iteration = _iteration;*/

			SHA_Encryption();
			sendPrivacy();
		}
		
		public void sendPrivacy(){
			_css.configurePacket("resistor");
			_size=5;
					
			byte[] _buf = new byte[3];
			_buf[0] = SIGN_UP;
			_buf[1]=2;
			_buf[2] = _size;
		 	/*ByteBuffer _message = ByteBuffer.allocateDirect(1024);
			ByteBuffer _name_byte = ByteBuffer.allocateDirect(1024);
			ByteBuffer _id_byte = ByteBuffer.allocateDirect(1024);
			ByteBuffer _password_byte = ByteBuffer.allocateDirect(1024);
			ByteBuffer _email_byte = ByteBuffer.allocateDirect(1024);

			Charset _charset = Charset.forName("UTF-8");
			
			_message.put(_buf);
			_name_byte.put( _name.getBytes());
			_name_byte=_charset.encode(_name).array();
			_id_byte.put(_id.getBytes());
			_password_byte.put(_password.getBytes());
			_email_byte.put(_email.getBytes());
			
			_message.flip();
			_name_byte.flip();
			_id_byte.flip();
			_password_byte.flip();
			_email_byte.flip();
			
			_css.setPacket("resistor", _message);
			_css.setPacket("resistor",_name_byte);
			_css.setPacket("resistor",_id_byte);
			_css.setPacket("resistor",_password_byte);
			_css.setPacket("resistor",_email_byte);
			*/
			Charset _charset = Charset.forName("UTF-8");
			
			_css.setPacket("resistor", _buf);
			_css.setPacket("resistor",_charset.encode(_name).array());
			_css.setPacket("resistor",_id.getBytes());
			_css.setPacket("resistor",_password.getBytes());
			_css.setPacket("resistor",_email.getBytes());
			
			_css.send("resistor");
		}
		public void SHA_Encryption(){
			setPWD();
			/*setName();
			setEmail();*/
		}
		/*private void setName()
		{
			try {
				_messageDigest = MessageDigest.getInstance("SHA-256");
				_temp_name = _name.getBytes();
				_messageDigest.update(_temp_name);
				this._name = new String(Function.Base64Coder.encode(_messageDigest.digest()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}*/

		private void setPWD()
		{
			try {
				_messageDigest = MessageDigest.getInstance("SHA-256");
				_temp_pwd = _password.getBytes();
				_messageDigest.update(_temp_pwd);
				_password = new String(Function.Base64Coder.encode(_messageDigest.digest()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}	

	/*	private void setEmail()
		{
			try {
				_messageDigest = MessageDigest.getInstance("SHA-256");
				_temp_email = _email.getBytes();
				_messageDigest.update(_temp_email);
				_email = new String(Function.Base64Coder.encode(_messageDigest.digest()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}*/
	}	
