package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Function.PacketRule;

import javax.swing.JPasswordField;


	public class Client_SignUp extends JFrame //Create new account
	//Create new page
	{											
		BufferedImage _img=null;
		SHA_256 _sha = null;
		
		public static void main(String[] args){
			new Client_SignUp();
		}
		

		private boolean _checkID = false;
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
		private String _id="id";
		private String _email="email";
		private String _password="password";
		private String _passwordCorrect="passwordCorrect";

		JButton _same;
		JButton _ok;
		JButton _cancel;

		Font fontblank = new Font ("SansSerif", Font.BOLD,13);
	
		Client_Server_Connector _css;

		public Client_SignUp(){
			try {
				_css=Client_Server_Connector.getInstance(4444);
			} catch (InterruptedException e1) {
				// TODO 자동 생성된 catch 블록
				e1.printStackTrace();
			}
			setTitle("CRYPTONITE");
			setBounds(710,200,508, 660);//Input value

			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			setBackground(Color.BLACK);

			try
			{
				_img = ImageIO.read(new File("D:\\crypto\\resistor.png"));//Input MainImage
				
			}catch(IOException e){
				System.out.println("No Image.");
				System.exit(0);
			}

			JLayeredPane _layeredpane =new JLayeredPane();
			_layeredpane.setBounds(0, 0, 490, 655);//input value
			_layeredpane.setLayout(null);

			Mypanel _panel = new Mypanel();
			_panel.setBounds(0,0,490,655);//input value  

			_nameField = new JTextField();
			_nameField.setBounds(193,290, 130, 20);//input value
			_nameField.setBorder(BorderFactory.createEmptyBorder());
			_nameField.setForeground(Color.BLACK);

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
			_idField.setBounds(193, 326, 130, 20);//input value

			_idField.setForeground(Color.BLACK);
			_idField.setBorder(BorderFactory.createEmptyBorder());
			//_idField.setOpaque(false);

			_idField.addKeyListener(new KeyListener()
			{
				@Override
				public void keyPressed(KeyEvent _e)  { }

				@Override
				public void keyReleased(KeyEvent _e) 
				{
					_id = _idField.getText();
				}

				@Override
				public void keyTyped(KeyEvent _e) { }
			}); 
			_layeredpane.add(_idField);

			_passwdField = new JPasswordField();
			_passwdField.setBounds(194, 365, 130, 20);//input value

			_passwdField.setEchoChar('●');
			_passwdField.setBorder(BorderFactory.createEmptyBorder());
			_passwdField.setForeground(Color.BLACK);
			//_passwdField.setOpaque(false);
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

			_passwdCorrectField = new JPasswordField();
			_passwdCorrectField.setBounds(194, 408, 130, 20);//input value
			_passwdCorrectField.setEchoChar('●');
			_passwdCorrectField.setBorder(BorderFactory.createEmptyBorder());
			_passwdCorrectField.setForeground(Color.BLACK);
			//_passwdCorrectField.setOpaque(false);
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
				else if( _passwdCorrectField.equals(_password) == false &&  _passwdCorrectField.equals(null) == false)
				{
					_checkPassword = false;
				}

			}

			@Override
			public void keyTyped(KeyEvent _e) { }

		});
		_layeredpane.add( _passwdCorrectField);

		_emailField = new JTextField();
		_emailField.setBounds(193, 455, 160, 20);//input value
		_emailField.setBorder(BorderFactory.createEmptyBorder());
		_emailField.setForeground(Color.BLACK);
		//_emailField.setOpaque(false);
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

		_same = new JButton(new ImageIcon(""));//Input IconImage
		_same.setRolloverIcon(new ImageIcon(""));
		_same.setBounds(356, 312, 80, 38);//input value
		_same.setBorderPainted(false);
		_same.setFocusPainted(false);
		_same.setContentAreaFilled(false);
		_same.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				/*try 
      		{
      			if(id.equals("init") == false)
      			{
					socket = new Socket(serverIP, serverPort);

					java.io.OutputStream out = socket.getOutputStream();
					DataOutputStream dos = new DataOutputStream(out);

					dos.writeInt(2);	// 중복확인 플래그 전달
					dos.flush();
					System.out.println("중복확인 플래그 2 전달");

					dos.writeUTF(id);	// 1번째
					dos.flush();

					InputStream in = socket.getInputStream();
					DataInputStream dis = new DataInputStream(in);

					checkID = dis.readBoolean();	// 2번째
					if(checkID == true)
					{
						showMessage("아이디 중복 없음", "중복되지 않는 아이디입니다.");
						goSignUP = true;
					}
					else if(checkID == false)
					{
						showMessage("아이디 중복", "이미 존재하는 아이디 입니다.");
					}

					dos.close();
					out.close();

					dis.close();
					in.close();
      			}
      			else
      			{
      				showMessage("입력 오류", "아이디를 입력해주세요.");
      			}

			} catch (UnknownHostException e) { e.printStackTrace();
			} catch (IOException e) { e.printStackTrace();
			}*/
			}
			//compare id
		});
		_layeredpane.add(_same);

		_cancel = new JButton(new ImageIcon(""));//Input IconImage
		_cancel.setRolloverIcon(new ImageIcon(""));
		_cancel.setPressedIcon(new ImageIcon(""));

		_cancel.setBounds(260, 544, 82, 38);//input value
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

		_ok = new JButton(new ImageIcon(""));//Input IconImage
		_ok.setRolloverIcon(new ImageIcon(""));
		_ok.setPressedIcon(new ImageIcon(""));

		_ok.setBounds(156, 544, 82, 38);//input value
		_ok.setBorderPainted(false);
		_ok.setFocusPainted(false);
		_ok.setContentAreaFilled(false);

		_ok.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent _arg0)
			{
				if(_name.equals("name") == false && _id.equals("id") == false && _password.equals("password") == false &&
						_passwordCorrect.equals("passwordCorrect") == false && _email.equals("email") == false/* && _goSignUP == true*/)
				{
					if(_checkPassword == true)
					{
						showMessage("가입 완료", "회원가입이 완료되었습니다. 로그인해주세요.");

						//userKeyGenerator _ukg = new userKeyGenerator();
						_sha = new SHA_256(_name,_id,_password,_email/*,_ukg.genEncAesKey(_password), _ukg.getSalt(), _ukg.getIterationCount()*/);

						dispose();
					}
					else if(_checkPassword == false)
					{
						showMessage("비밀번호 오류", "비밀번호가 일치하지 않습니다.");
					}

				}
				else
				{
					showMessage("가입 오류", "모든 항목을 다 입력하지않았거나, 아이디 중복입니다.");
				}
			}
		});

		_layeredpane.add(_ok);
		_layeredpane.add(_panel);//패널1을 레이아웃에 넣기
		getContentPane().add(_layeredpane);


		setVisible(true);
	}
	class Mypanel extends JPanel
	{
		public void paint(Graphics _g)
		{
			_g.drawImage(_img,0,0,null);
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
				// TODO 자동 생성된 catch 블록
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
					
			byte[] buf = new byte[2];
			buf[0] = SIGN_UP;
			buf[1] = _size;
			
			ByteBuffer _message = ByteBuffer.allocateDirect(2);
			ByteBuffer _name_byte = ByteBuffer.allocateDirect(100);
			ByteBuffer _id_byte = ByteBuffer.allocateDirect(_id.length());//id 수만큼
			ByteBuffer _password_byte = ByteBuffer.allocateDirect(50);
			ByteBuffer _email_byte = ByteBuffer.allocateDirect(100);

			Charset _charset = Charset.forName("UTF-8");
			
			_message.put(buf);
			_name_byte.put( _name.getBytes());//바이트로 바꾸기.
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
			
			_css.send("resistor");
		}
		public void SHA_Encryption(){
			setName();
			setPWD();
			setEmail();
		}
		private void setName()
		{
			try {
				_messageDigest = MessageDigest.getInstance("SHA-256");
				_temp_name = _name.getBytes();
				_messageDigest.update(_temp_name);
				this._name = new String(Function.Base64Coder.encode(_messageDigest.digest()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

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

		private void setEmail()
		{
			try {
				_messageDigest = MessageDigest.getInstance("SHA-256");
				_temp_email = _email.getBytes();
				_messageDigest.update(_temp_email);
				_email = new String(Function.Base64Coder.encode(_messageDigest.digest()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}	
