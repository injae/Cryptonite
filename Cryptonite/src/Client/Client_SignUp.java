package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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

import Function.PacketProcessor;
import Function.PacketRule;
import Server.Server_DataBase;
import Crypto.Base64Coder;
import Crypto.Crypto;
import Crypto.aesKeyGenerator;

	public class Client_SignUp extends JFrame implements PacketRule //Create new account
	//Create new page
	
	{											
		BufferedImage _img=null;
		SHA_256 _sha = null;
		
		public static void main(String[] args){
			new Client_SignUp();
		}
		private boolean _checkBlank=false;
		private boolean _checklength=true;
		private boolean _checkSame=false;
		private boolean _checkemail=false;
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
		
		JTextField _nameField;
		JTextField _idField;
		JTextField _emailField;
		JPasswordField _passwdField;
		JPasswordField _passwdCorrectField;
		
		
		private String _name="";
		private String _id="";
		private String _email="";
		private String _password="";
		private String _passwordCorrect="";
		
		Client_Server_Connector _csc;

		JButton _same;
		JButton _ok;
		JButton _cancel;

		 Font _font = new Font ("SansSerif", Font.BOLD,17);
		 Font _precondition_font = new Font ("Dialog", Font.BOLD,16);
	
		Client_Server_Connector _css;

		public Client_SignUp(){
			                                              
	
			_csc=Client_Server_Connector.getInstance();
	
			setTitle("CRYPTONITE");
			setBounds(710,200,456,665);
			setResizable(false);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			setBackground(Color.BLACK);
			setLocationRelativeTo(null);
			try{
				 Toolkit tk = Toolkit.getDefaultToolkit(); 
				 Image image = tk.getImage("gui/logo.png");
				 this.setIconImage(image);
			}
			catch(Exception e)
			{
				System.out.println("Appilcation icon not found");
			}
			try
			{
				_img = ImageIO.read(new File("gui/signup_main.png"));
				
			}catch(IOException e){
				System.out.println("Image Load Failed.");
				System.exit(0);
			}

			JLayeredPane _layeredpane =new JLayeredPane();
			_layeredpane.setBounds(0, 0, 490,655);//input value
			_layeredpane.setLayout(null);

			Mypanel _panel = new Mypanel();
			_panel.setBounds(0,0,490,655);

			_nameField = new JTextField();
			_nameField.setBounds(170, 200, 160, 21);
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

		_same = new JButton(new ImageIcon("gui/check_bt.png"));
		//_same.setRolloverIcon(new ImageIcon("gui/_check_bt.png"));
		_same.setBounds(320, 250, 80, 38);
		_same.setBorderPainted(false);
		_same.setFocusPainted(false);
		_same.setContentAreaFilled(false);
		_same.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try 
				{
					byte size=2;
					byte[] event=new byte[1024];
					event[0] =SIGN_UP;
					event[1]=1;
					event[2]=size;
					
					_csc.send.setPacket(event).write();
					_csc.send.setPacket(_id.getBytes(),500).write();	
					byte[] Checkid = _csc.receive.setAllocate(2).read().getByte();
					System.out.println(Checkid[0]);
					if(_id.equals(""))
					{ 
						showMessage("ERROR", "Please insert ID");
					}
					else
					{
						switch(Checkid[0])
						{
						case 1 :
							_checkSame=true;
							showMessage("ID", "This ID can be used."); 
							break;
						case 2:
							showMessage("ERROR", "This ID is already in use. Please use another ID.");
							break;
						}
					}
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			
			}
		});
		_layeredpane.add(_same);

		_cancel = new JButton(new ImageIcon("gui/cancel_bt.png"));
		_cancel.setRolloverIcon(new ImageIcon("gui/cancel_bt_hv.png"));

		_cancel.setBounds(178, 560, 90, 50);//input value
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

		_ok = new JButton(new ImageIcon("gui/signup_bt.png"));
		_ok.setRolloverIcon(new ImageIcon("gui/signup_bt_hv.png"));

		_ok.setBounds(25, 500, 400, 50);//input value
		_ok.setBorderPainted(false);
		_ok.setFocusPainted(false);
		_ok.setContentAreaFilled(false);

		_ok.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent _arg0)
			{
				if(_email.contains("@")){ _checkemail=true; }
				
				if(_password.length()<5||_passwordCorrect.length()<5){
					_checklength=false;
				}
				else if(_password.length()>=5&&_passwordCorrect.length()>=5){
					_checklength=true;
				}
				
				if(_passwordCorrect.equals(_password))
				{
					_checkPassword = true;
				}
				else /*if( _passwdCorrectField.equals(_password) == false ||  _passwdCorrectField.equals(null) == false)*/
				{
					_checkPassword = false;
				}
				
				if(!_name.equals("") && !_id.equals("")&& !_password.equals("") &&!_passwordCorrect.equals("") 
						&& !_email.equals(""))
				{
					_checkBlank=true;
				}
				else{_checkBlank=false;}
				
				if(_checkBlank)
				{
					if(_checkSame)
					{
						if(_checklength)
						{
							if(_checkPassword)
							{
								if(_checkemail){
									_goSignUP=true;
								}
								else{ showMessage("ERROR", "Email does not meet the conditions."); }
							}
							else { showMessage("ERROR", "Passcodes did not match."); }
						}
						else{ showMessage("ERROR", "Please check the requirements of the password."); }
					}
					else{ showMessage("ERROR", "Check whether the duplicates ID");}
				}
				else{ showMessage("ERROR", "Please fill in all the blanks.");}

				if(_goSignUP){

					/*userKeyGenerator _ukg = new userKeyGenerator();
					_ukg.init();*/
					_sha = new SHA_256(_name,_id,_password,_email);
					
					int result=_sha.result[0];
					
					if(result==1)
					{
						dispose();
						_goSignUP=false;
					}
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
			_g.drawString("->Please enter at least 5 characters.", 100, 355);
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

		private String _AES_Key;
		private String _salt;
		private String _iteration;

		private byte[] _temp_name;
		private byte[] _temp_pwd;
		private byte[] _temp_email;
		
		private byte[] _temp_data;
		private byte _size;
		byte[] result;

		public SHA_256(String _name, String _id, String _password, String _email)
		{	

			_css=Client_Server_Connector.getInstance();

			this._name=_name;
			this._id = _id;
			this._password = _password;
			this._email = _email;

			SHA_Encryption();
			sendPrivacy();
		}
		
		public void sendPrivacy(){
			try
			{
				Charset _charset = Charset.forName("UTF-8");

						
				byte[] _buf = new byte[1024];
				_buf[0] = SIGN_UP;
				_buf[1]=2;
				_buf[2] = 5;
			
				_css.send.setPacket(_buf).write();
				
				_css.send.setPacket(_charset.encode(_name).array(),500).write();
				_css.send.setPacket(_id.getBytes(),500).write();
				_css.send.setPacket(_password.getBytes(),500).write();
				_css.send.setPacket(_email.getBytes(),500).write();

				
				result = _css.receive.setAllocate(1).read().getByte();
				
				if(result[0] == 1)
				{
					System.out.println("signUp Success");
					showMessage("WELCOME!", "WELCOME TO CRYPTONITE!.");
				}
				else
				{
					System.out.println("signUp Fail");
					showMessage("Cryptonite", "Failed to SignUp.");
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		
		}
		public void SHA_Encryption(){
			setPWD();
		}
		
		private void setPWD()
		{
			try {
				_messageDigest = MessageDigest.getInstance("SHA-256");
				_temp_pwd = _password.getBytes();
				_messageDigest.update(_temp_pwd);
				_password = new String(Base64Coder.encode(_messageDigest.digest()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}	
		
		private void showMessage(String _title, String _message) 
		{
			JOptionPane.showMessageDialog(null, _message, _title, JOptionPane.INFORMATION_MESSAGE);
		}

	
	}	
