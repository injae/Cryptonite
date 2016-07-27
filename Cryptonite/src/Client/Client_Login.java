package Client;

<<<<<<< HEAD
public class Client_Login 
{
	
=======
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
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;






public class Client_Login extends JFrame
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
    
    boolean _firstTime = true;
    
    JTextField _loginField;
    JPasswordField _passwordField;
    
    private String _id = "id";
    private String _password = "password";
    private String _tempPassword = "init";
    
    /*//private Client_FolderChooser_UI fc = null;
    
    // 로그인 카운터 읽어주기 위한것
    private FileReader fr = null;
    private String loginCount = null;
    private StringTokenizer st = null;
    
    // 로그인 횟수가 2번이상일 경우 여기서 스레드 실행
    private Client_FolderScan cfs = null;
    private Client_SendFiles csf = null;
    private Client_checkEncryptionAnime cea = null;
    private Client_FileShare_Send cfss = null;
    
    // 메인프레임 UI
    private boolean mainFrameFlag = false;
    private Client_MainFrame_UI cmfu = null;
    
    // AES_Key 추출관련
    private User loginedUser = null;
    private byte[] AES_Key = null;
    */
    // 맥 어드레스 추출
    
    Font _font1 = new Font("SansSerif", Font.BOLD, 25);
    Font _fontjoin = new Font("SansSerif", Font.BOLD,13);
    Font _fontid = new Font ("SansSerif", Font.BOLD,15);
    
    JButton _Login;      
    JButton _Resistor;
    
    JRadioButton _individual = new JRadioButton();
    JRadioButton _group = new JRadioButton();
    public ButtonGroup _buttonGroup = new ButtonGroup();
    
    public Client_Login(){
    	setTitle("Cryptonite");
        setBounds(710,200,470,645);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        // 레이아웃 설정
        getContentPane().setLayout(null);
        JLayeredPane _layeredPane = new JLayeredPane();
        _layeredPane.setBounds(0, 0, 470, 645);
        _layeredPane.setLayout(null);

        // 패널1
        // 이미지 받아오기
        try {
            _img = ImageIO.read(new File("D:\\crypto\\login.png"));//input image
        } catch (IOException e) {
            System.out.println("No Image");
            System.exit(0);
        }
         
        MyPanel _panel = new MyPanel();
        _panel.setBounds(0, 0, 470, 645);
        
        _buttonGroup.add(_individual);
        _buttonGroup.add(_group);
        
        _individual.setBounds(180, 300, 20, 20);//설정하기
        _individual.setBorder(BorderFactory.createEmptyBorder());
        _individual.setOpaque(false);
        _group.setBounds(300, 300, 20, 20);//설정하기
        _group.setBorder(BorderFactory.createEmptyBorder());
        _group.setOpaque(false);
        
        _layeredPane.add(_individual);
        _layeredPane.add(_group);
  
        
        _loginField = new JTextField(15);
        _loginField.setBounds(134, 327, 200, 30);
        _layeredPane.add(_loginField);
        _loginField.setOpaque(false);
        _loginField.setForeground(Color.blue);
        _loginField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        _loginField.setHorizontalAlignment(JTextField.CENTER);
        _loginField.setText("ID를 입력하시오.");
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
         _passwordField.setBounds(136, 395, 200, 30);
         _passwordField.setOpaque(false);
         _passwordField.setForeground(Color.white);
         _passwordField.setFont(_fontid);
         _passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
         _passwordField.setHorizontalAlignment(JTextField.CENTER);
         _passwordField.setEchoChar((char)0);
         _passwordField.setText("PASSWORD를 입력하시오.");
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
     			_password = new String(_passwordField.getPassword());
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
         
         _Login = new JButton(new ImageIcon(""));//input buttonimage
         _Login.setBounds(193, 449, 80, 37);
         _Login.setBorderPainted(false);
         _Login.setFocusPainted(false);
         _Login.setContentAreaFilled(false);
         _Login.setRolloverIcon(new ImageIcon(""));//input buttonimage
         _Login.addMouseListener(new MouseAdapter(){
          	public void mouseClicked(MouseEvent e){
          		if(_individual.isSelected()){
          			//개인
          		}
          		if(_group.isSelected()){
          			//그룹
          		}
          	}
         });
         _Resistor = new JButton(new ImageIcon("C:\\cryptonite\\img\\SignUp1.png"));
         _Resistor.setFont(_fontjoin);
         _Resistor.setForeground(Color.white);
         _Resistor.setBounds(153,516,155,49);
         _Resistor.setBorderPainted(false);
         _Resistor.setFocusPainted(false);
         _Resistor.setContentAreaFilled(false);
         _Resistor.setRolloverIcon(new ImageIcon("C:\\cryptonite\\img\\SignUp2.png"));
         _Resistor.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent arg0) {
         		new Client_SignUp();
         	}
         });
        
         _layeredPane.add(_Login);
         _layeredPane.add(_Resistor);
         // 마지막 추가들
         _layeredPane.add(_panel);
              
         getContentPane().add(_layeredPane);          
         setVisible(true);
          	
    }    
>>>>>>> refs/remotes/origin/LeeDEV
}
    

