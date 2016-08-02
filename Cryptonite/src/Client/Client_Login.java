
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
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

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

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import Function.Base64Coder;
import Server.Server_DataBase;



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
    
	private boolean _checkLogin =false;
    private boolean _firstTime = true;
    
    JTextField _loginField;
    JPasswordField _passwordField;
    
    private String _id = "id";
    private String _password = "password";
    private String _tempPassword = "init";

    
    /*//private Client_FolderChooser_UI fc = null;
    
    // 로그인 카운터 읽어주기 위한 것
    private FileReader fr = null;
    private String loginCount = null;
    private StringTokenizer st = null;
    
    // 로그인 횟수가 2번이상일 경우 여기서 스레드 실행
    private Client_FolderScan cfs = null;
    private Client_SendFiles csf = null;
    private Client_checkEncryptionAnime cea = null;
    private Client_FileShare_Send cfss = null;
    
    // 메인 프레임 UI
    private boolean mainFrameFlag = false;
    private Client_MainFrame_UI cmfu = null;
    
    // AES_Key 추출 관련
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
    
    private void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
    
    public Client_Login(){
    	setTitle("Cryptonite");
        setBounds(710,200,470,645);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 

        
        getContentPane().setLayout(null);
        JLayeredPane _layeredPane = new JLayeredPane();
        _layeredPane.setBounds(0, 0, 470, 645);
        _layeredPane.setLayout(null);

      
        try {
            _img = ImageIO.read(new File("D:\\crypto\\_login.png"));//input image
        } catch (IOException e) {
            System.out.println("No Image");
            System.exit(0);
        }
         
        MyPanel _panel = new MyPanel();
        _panel.setBounds(0, 0, 470, 645);
        
        _buttonGroup.add(_individual);
        _buttonGroup.add(_group);
        
        _individual.setBounds(180, 300, 20, 20);
        _individual.setBorder(BorderFactory.createEmptyBorder());
        _individual.setOpaque(false);
        _group.setBounds(300, 300, 20, 20);
        _group.setBorder(BorderFactory.createEmptyBorder());
        _group.setOpaque(false);
        
        _layeredPane.add(_individual);
        _layeredPane.add(_group);
  
        
        _loginField = new JTextField(15);
        _loginField.setBounds(134, 327, 200, 30);
        _layeredPane.add(_loginField);
        _loginField.setOpaque(false);
        _loginField.setForeground(Color.WHITE);
        _loginField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        _loginField.setHorizontalAlignment(JTextField.CENTER);
        _loginField.setText("ID");
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
         _passwordField.setText("PASSWORD");
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
          		
          		Server_DataBase _db;
				_db=Server_DataBase.getInstance();
				_db.Init_DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/"+"cryptonite", "root", "yangmalalice3349!");
				_db.connect();
				

				Connection _con=(Connection) _db.Getcon();
				PreparedStatement _ps = null;
				ResultSet _rs = null;        
				String _sql = "select * from test";
				
				try{
					_ps = (PreparedStatement)_con.prepareStatement(_sql);
					_rs = _ps.executeQuery(); // 쿼리문이 select 로 시작되면 무조건
					System.out.println("id\tpassword\tenc_password");
					
					while(_rs.next()){
						String _get_id = _rs.getString(2);
						String _get_pwd = _rs.getString(3);// 두번째 필드의 데이터
						String _enc_pwd=Encode_password(_password);
						System.out.println(_get_id+"\t"+_get_pwd+"\t"+_enc_pwd);
						
						if(_get_id.equals(_id)&&_enc_pwd.equals(_get_pwd))
						{
							_checkLogin=true;
						}
					}
				}catch(SQLException e1){
					e1.printStackTrace();
				}
				if(_checkLogin==true){
					showMessage("LOGIN", "Welcome,\t"+_id);
				}
          		/*if(id.equals("init") == false && password.equals("init") == false){
         			try 
         			{
         				tempPassword = password;		// 임시
						MessageDigest md = MessageDigest.getInstance("SHA-256");
						byte[] temp = password.getBytes();
						md.update(temp);
						password = new String(Base64Coder.encode(md.digest()));
						System.out.println("password : " + password);
						sl = new SendLogin(id,password);
					} 
         			catch (NoSuchAlgorithmException e2) 
         			{
						e2.printStackTrace();
					}
         			
             		if(sl.getNextLogin() == true){
             			notifyLogin nl = new notifyLogin();
             			showMessage("로그인 완료", "로그인이 완료되었습니다.");
             			nl.login();
             			cmfu = new Client_MainFrame_UI(getLoginFrame(), id);
             			
             			if(sl.getLoginCount() == 1){
             				showMessage("최초 로그인", "최초로그인 이므로 보호폴더 지정을 하셔야 합니다.");
             				fc = new Client_FolderChooser_UI(tempPassword,nl,cmfu);
             				cmfu.mainFrameUI_ON();
                 			cmfu.setEnabled(false);
             			}
             			
             			if(sl.getLoginCount() >= 2){
             				cfs = new Client_FolderScan();
             				loginedUser = nl.getUserObejct();
             				AES_Key = loginedUser.getAesKey();
             				PBE pbe = new PBE(loginedUser.getSalt(), 1000);
             				try 
             				{
								cfs.setAES_Key(pbe.decrypt(tempPassword, AES_Key));
							} 
             				catch (GeneralSecurityException e1) 
             				{
								e1.printStackTrace();
							}
             				
             				cmfu.mainFrameUI_ON();
                 			cmfu.setEnabled(true);
             				
             				csf = new Client_SendFiles();
             				cea = new Client_checkEncryptionAnime();
             				
             				cfs.start();
             				csf.start();
             				cea.start();		// 로그인 횟수가 2회 이상일경우 여기서 스레드 시작
             			}
             			dispose();
             			mainFrameFlag = true;
             		}
             		else{
             			if(sl.getPathMac() != 0)	// 수정
             				showMessage("로그인 실패", "존재하지 않는 아이디거나, 비밀번호가 틀렸습니다.");
             		}
         		}
         		else{
         			showMessage("로그인 오류", "모든 항목을 입력해주세요.");
         		}*/
          		if(_individual.isSelected()){
          			
          		}
          		if(_group.isSelected()){
          			
          		}
          	}
         });
         _Resistor = new JButton(new ImageIcon(""));
         _Resistor.setFont(_fontjoin);
         _Resistor.setForeground(Color.white);
         _Resistor.setBounds(153,516,155,49);
         _Resistor.setBorderPainted(false);
         _Resistor.setFocusPainted(false);
         _Resistor.setContentAreaFilled(false);
         _Resistor.setRolloverIcon(new ImageIcon(""));
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
