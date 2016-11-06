package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.lang.model.element.PackageElement;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.mysql.jdbc.log.Log;

import Crypto.Base64Coder;
import Crypto.KeyReposit;
import Function.PacketRule;
import Server.Server_DataBase;

public class Client_Login extends JFrame implements PacketRule 
{
	public static void main(String[] args) 
	{
		new Client_Login();
	}

	BufferedImage _img = null;

	class MyPanel extends JPanel 
	{
		public void paint(Graphics g) 
		{
			g.drawImage(_img, 0, 0, null);
		}
	}
	
	public static final Queue<Client_FolderScan> _folderScanList = new LinkedList<Client_FolderScan>();
	public static final Queue<String> _autoBackupList = new LinkedList<String>();

	private boolean _checkLogin = false;
	private boolean _firstTime = false;

	JTextField _idField;
	JPasswordField _passwordField;

	private String _id = "";
	private String _password = "";
	private String _address = "";

	private Client_Server_Connector _csc;
	private Client_FolderSelector _cfs = null;
	private Client_FolderScan _cfc = null;

	private byte[] _gpcount = null;

	private String _name = null;
	private String _uscode = null;
	private byte[] _aeskey = null;

	private ArrayList<String> _gpcode = new ArrayList<String>();
	private ArrayList<String> _gpname = new ArrayList<String>();

	Font _font1 = new Font("SansSerif", Font.BOLD, 23);
	Font _fontjoin = new Font("SansSerif", Font.BOLD, 23);
	Font _fontid = new Font("SansSerif", Font.PLAIN, 23);

	JButton _Login;
	JButton _Registor;

	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,25);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public Client_Login() 
	{
		try 
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getImage("gui/logo.png");
			this.setIconImage(image);
		} 
		catch (Exception e) 
		{
			System.out.println("Appilcation icon not found");
		}

		_csc = Client_Server_Connector.getInstance();

		setTitle("Cryptonite");
		setBounds(710, 200, 456, 665);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(null);
		JLayeredPane _layeredPane = new JLayeredPane();
		_layeredPane.setBounds(0, 0, 470, 700);
		_layeredPane.setLayout(null);

		try {
			_img = ImageIO.read(new File("gui/login_main.png"));// input image
		} catch (IOException e) {
			System.out.println("No Image");
			System.exit(0);
		}

		MyPanel _panel = new MyPanel();
		_panel.setBounds(0, 0, 470, 700);
		_idField = new JTextField(15);
		_idField.setFont(_font1);
		_idField.setBounds(136, 245, 200, 31);
		_layeredPane.add(_idField);
		_idField.setOpaque(false);
		_idField.setForeground(Color.BLACK);
		_idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		_idField.setHorizontalAlignment(JTextField.CENTER);
		_idField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode() == KeyEvent.VK_ENTER) 
				 {
					 LoginButton();
				 }
			}

			@Override
			public void keyReleased(KeyEvent e) {
				_id = _idField.getText();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		_idField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			}
		});

		_passwordField = new JPasswordField(15);
		_passwordField.setBounds(138, 310, 200, 20);
		_passwordField.setFont(_font1);
		_passwordField.setOpaque(false);
		_passwordField.setForeground(Color.BLACK);
		_passwordField.setFont(_fontid);
		_passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		_passwordField.setHorizontalAlignment(JTextField.CENTER);
		_passwordField.setEchoChar((char) 0);
		// _passwordField.setText("PASSWORD");
		_passwordField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent fe) {
				_passwordField.setEchoChar('●');
			}
		});
		_passwordField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode() == KeyEvent.VK_ENTER) 
				 {
					 LoginButton();
				 }
			}

			@Override
			public void keyReleased(KeyEvent e) {
				_password = new String(_passwordField.getPassword());
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		_layeredPane.add(_passwordField);

		_Login = new JButton(new ImageIcon("gui/login_bt.png"));// input
		_Login.setRolloverIcon(new ImageIcon("gui/login_bt_hv.png"));
		_Login.setBounds(30, 400, 400, 50);
		_Login.setBorderPainted(false);
		_Login.setFocusPainted(false);
		_Login.setContentAreaFilled(false);
		_Login.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode() == KeyEvent.VK_ENTER) 
				 {
					 LoginButton();
				 }
			}
		});
		_Login.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				LoginButton();
			}

		});
		_Registor = new JButton(new ImageIcon("gui/register_bt.png"));
		_Registor.setRolloverIcon(new ImageIcon("gui/register_bt_hv.png"));
		_Registor.setFont(_fontjoin);
		_Registor.setForeground(Color.white);
		_Registor.setBounds(188, 480, 80, 37);
		_Registor.setBorderPainted(false);
		_Registor.setFocusPainted(false);
		_Registor.setContentAreaFilled(false);
		_Registor.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				new Client_SignUp();
			}
		});

		_layeredPane.add(_Login);
		_layeredPane.add(_Registor);
		_layeredPane.add(_panel);

		getContentPane().add(_layeredPane);
		setVisible(true);

	}

	public String Encode_password(String _password) {
		MessageDigest _md = null;
		try {
			_md = MessageDigest.getInstance("SHA-256");
			byte[] temp = _password.getBytes();
			_md.update(temp);
			_password = new String(Base64Coder.encode(_md.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return _password;
	}
	
	public String getID()
	{
		return _id;
	}
	
	public void LoginButton()
	{
		if (!_id.equals("") && !_password.equals("")) {
			try {
				byte size = 3;
				byte[] event = new byte[1024];
				event[0] = LOGIN;
				event[1] = size;
				_csc.send.setPacket(event).write();
				_csc.send.setPacket(_id.getBytes(), 500).write();// ����
				_password = Encode_password(_password);
				_csc.send.setPacket(_password.getBytes(), 500).write();

				System.out.println(_id + "\t" + _password);

				byte[] checkLogin = _csc.receive.read().getByte();
				switch (checkLogin[0]) {
				case 1:
					showMessage("Error", "No id");
					_checkLogin = false;
					break;
				case 2:
					showMessage("LOGIN", "Welcome,\t" + _id);

					_gpcount = _csc.receive.setAllocate(100).read().getByte();

					_name = _csc.receive.setAllocate(500).read().getByte().toString().trim();

					_aeskey = _csc.receive.setAllocate(32).read().getByte();
					(KeyReposit.getInstance()).set_aesKey(new SecretKeySpec(_aeskey, "AES"));

					for (int i = 0; i < _gpcount[0]; i++) {
						_gpcode.add(new String(_csc.receive.setAllocate(100).read().getByte()).trim());
						_gpname.add(new String(_csc.receive.setAllocate(500).read().getByte()).trim());
					}

					if (checkLogin[1] == 1)// count 1�϶�
					{
						showMessage("FIRST LOGIN", "CONGURATULATION! FIRST LOGIN!");
						_cfs = new Client_FolderSelector();
						_cfs.folderSelectorON();
						while (!_cfs.getSelectionEnd()) 
						{
							try 
							{
								Thread.sleep(1);
							}
							catch (InterruptedException e1) 
							{
								e1.printStackTrace();
							}
						}
						_address = _cfs.getSelectedPath();
						File save = new File("Cryptonite_Client/log/protectedlog.ser");
						FileWriter fw = new FileWriter(save);
						fw.write(_address);
						fw.close();
						Client_Icon_Change.change(_address);
						Thread.sleep(1000);
					}
					_cfc = new Client_FolderScan();
					Client_Login._folderScanList.offer(_cfc);
					_cfc.start();
					dispose();
					new Client_Main_UI(_gpcode, _gpname, _name, _uscode, _id, _cfc);
					KeyReposit.getInstance();
					break;
				case 3:
					showMessage("Error", "Wrong password");
					_checkLogin = false;
					break;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			showMessage("Login", "Please insert id or password");

		}

	}
}

/*
 * class Mymouse implements MouseListener{ JButton Login; JButton Resistor;
 * public Mymouse(JButton _Login, JButton _Registor){ this.Login=_Login;
 * this.Resistor=_Registor; } public void mouseEntered(MouseEvent e){
 * if(e.getSource() ==Login ){ Login.setIcon(new
 * ImageIcon("img/login_bt_hv.png"));} if(e.getSource() == Resistor){
 * Resistor.setIcon(new ImageIcon("img/register_bt_hv.png"));} }
 * 
 * @Override public void mouseClicked(MouseEvent arg0) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void mouseExited(MouseEvent e) { // TODO Auto-generated
 * method stub if(e.getSource() == Login) { Login.setIcon(new ImageIcon(
 * "img/chatting server.png")); } if(e.getSource() == Resistor) {
 * Resistor.setIcon(new ImageIcon("img/chatting.png")); } }
 * 
 * @Override public void mousePressed(MouseEvent e) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void mouseReleased(MouseEvent e) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * }
 */