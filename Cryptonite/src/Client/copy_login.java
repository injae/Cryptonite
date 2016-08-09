package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Client.Client_Login.MyPanel;
import Function.Base64Coder;
import Function.PacketProcesser;
import Function.PacketRule;

public class copy_login extends JFrame implements PacketRule
{
	public static void main(String[] args){
		new Client_Login();
	}
	BufferedImage _img = null;
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(_img, 0, 0, null);
        }
    }
    
	private boolean _checkLogin =false;
    private boolean _firstTime = true;
    
    JTextField _loginField;
    JPasswordField _passwordField;
    
    private String _id = "id";
    private String _password = "password";
    private String _tempPassword = "init";


    // 맥 어드레스 추출
    
    Font _font1 = new Font("SansSerif", Font.BOLD, 25);
    Font _fontjoin = new Font("SansSerif", Font.BOLD,13);
    Font _fontid = new Font ("SansSerif", Font.BOLD,15);
    
    JButton _Login;      
    JButton _Resistor;
    
    JRadioButton _individual = new JRadioButton();
    JRadioButton _group = new JRadioButton();
    public ButtonGroup _buttonGroup = new ButtonGroup();
    
    private void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
    
    public copy_login()
    {
    	setTitle("Cryptonite");
        setBounds(710,200,470,645);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        getContentPane().setLayout(null);
        JLayeredPane _layeredPane = new JLayeredPane();
        _layeredPane.setBounds(0,0, 470, 645);
        _layeredPane.setLayout(null);

      
        try {
            _img = ImageIO.read(new File("gui/login_main.png"));//input image
        } catch (IOException e) {
            System.out.println("No Image");
            System.exit(0);
        }
         
        MyPanel _panel = new MyPanel();
        _panel.setBounds(0, 0, 470, 645);
        
        _buttonGroup.add(_individual);
        _buttonGroup.add(_group);
        
        _individual.setBounds(180, 210, 20, 20);
        _individual.setBorder(BorderFactory.createEmptyBorder());
        _individual.setOpaque(false);
        _group.setBounds(300, 210, 20, 20);
        _group.setBorder(BorderFactory.createEmptyBorder());
        _group.setOpaque(false);
        
        _layeredPane.add(_individual);
        _layeredPane.add(_group);
  
        
        _loginField = new JTextField(15);
        _loginField.setBounds(140, 255, 190, 30);
        _layeredPane.add(_loginField);
        _loginField.setOpaque(false);
        _loginField.setForeground(Color.BLACK);
        _loginField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        _loginField.setHorizontalAlignment(JTextField.CENTER);
        //_loginField.setText("");
        _loginField.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) {
     			_id = _loginField.getText();
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {
     			if(_firstTime == true){
     				_loginField.setText("");
     				_firstTime = false;
     			}
     		}
           });
         _loginField.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		_loginField.setText("");
         	}
         });
        
         _passwordField = new JPasswordField(15);
         _passwordField.setBounds(140, 315, 200, 20);
         _passwordField.setOpaque(false);
         _passwordField.setForeground(Color.BLACK);
         _passwordField.setFont(_fontid);
         _passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
         _passwordField.setHorizontalAlignment(JTextField.CENTER);
         _passwordField.setEchoChar((char)0);
         //_passwordField.setText("PASSWORD");
         _passwordField.addFocusListener(new FocusAdapter(){
        	 public void focusGained(FocusEvent fe){
        		 _passwordField.setText("");
        		 _passwordField.setEchoChar('●');
        	 }
         });
         _passwordField.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) {
     			_password = Encode_password(new String(_passwordField.getPassword()));
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
           });
         _layeredPane.add(_passwordField);
         
         _passwordField.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		_passwordField.setText("");
         		_passwordField.setEchoChar('●');
         	}
         });
         
         _Login = new JButton(new ImageIcon("gui/login_bt.png"));//input buttonimage
         _Login.setBounds(30, 400, 400, 50);
         _Login.setBorderPainted(false);
         _Login.setFocusPainted(false);
         _Login.setContentAreaFilled(false);
         //_Login.setRolloverIcon(new ImageIcon("img/login_bt_hv"));//input buttonimage
         _Login.setPressedIcon(new ImageIcon("img/login_bt_hv.png"));
         _Login.addMouseListener(new MouseAdapter(){
          	public void mouseClicked(MouseEvent e)
          	{
          		SocketChannel s = null;
				try {
					s = SocketChannel.open();
					s.configureBlocking(true);
	    			s.connect(new InetSocketAddress("localhost", 4444));
	    			
				} catch (IOException e2) {
					// TODO 자동 생성된 catch 블록
					e2.printStackTrace();
				}
          		PacketProcesser p = new PacketProcesser(s, false);          	
          		byte[] event = new byte[1024];
          		event[0]=LOGIN;
          		event[1]=3;          		
          		p.setPacket(event).write();
          		p.setPacket(_id.getBytes()).write();
          		p.setPacket(_password.getBytes()).write();    
          		

          		byte[] checkLogin =  p.read().getByte();
          		switch(checkLogin[0]){
          		case 1 :
          			showMessage("Error", "No id");
          			break;
          		case 2 : 
          			showMessage("LOGIN", "Welcome,\t"+_id); 
          			break;
          		case 3 : 
          			showMessage("Error", "Wrong password"); 
          		}
          		if(_individual.isSelected()){
          			
          		}
          		if(_group.isSelected()){
          			
          		}          		
         }
         });
         _Resistor = new JButton(new ImageIcon("gui/register_bt.png"));
         _Resistor.setFont(_fontjoin);
         _Resistor.setForeground(Color.white);
         _Resistor.setBounds(184,480,80,37);
         _Resistor.setBorderPainted(false);
         _Resistor.setFocusPainted(false);
         _Resistor.setContentAreaFilled(false);
         //_Resistor.setRolloverIcon(new ImageIcon("img/_joinbtover"));
         _Resistor.setPressedIcon(new ImageIcon("gui/register_bt_hv.png"));
         _Resistor.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent arg0) {
         		new Client_SignUp();
         	}
         });
        
         _layeredPane.add(_Login);
         _layeredPane.add(_Resistor);
         _layeredPane.add(_panel);
              
         getContentPane().add(_layeredPane);          
         setVisible(true);
          	
    }	    
    
    
    public String Encode_password(String _password){
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
}
