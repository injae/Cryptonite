package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Client.Client_Login.MyPanel;

public class Client_TestOTP extends JFrame{
	public static void main(String[] args){
		new Client_TestOTP();
	}
	
	BufferedImage _img = null;
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(_img, 0, 0, null);
        }
    }
	JButton _OK;   
	
	JTextField _OTPField;
	
	int OTP;
	
	public Client_TestOTP(){
		try{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}	
		setTitle("Cryptonite");
        setBounds(710,200,600,300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        getContentPane().setLayout(null);
        JLayeredPane _layeredPane = new JLayeredPane();
        _layeredPane.setBounds(0,0, 600, 300);
        _layeredPane.setLayout(null);


        try {
            _img = ImageIO.read(new File("img/test_otp.jpg"));//input image
        } catch (IOException e) {
            System.out.println("No Image");
            System.exit(0);
        }
        
        MyPanel _panel = new MyPanel();
        _panel.setBounds(0, 0, 600, 300);
        
        _OTPField = new JTextField(15);
        _OTPField.setBounds(190, 70, 200, 31);
        _OTPField.setOpaque(true);
        _OTPField.setForeground(Color.WHITE);
        _OTPField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        _OTPField.setHorizontalAlignment(JTextField.CENTER);
        _OTPField.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) {
     			OTP = Integer.parseInt(_OTPField.getText());
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
           });
        	_OTPField.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		 _OTPField.setText("");
         	}
         });
        _layeredPane.add( _OTPField);
        
        _OK = new JButton(new ImageIcon("gui/login_bt.png"));//input buttonimage
        _OK.setBounds(100, 150, 400, 50);
        _OK.setBorderPainted(false);
        _OK.setFocusPainted(false);
        _OK.setContentAreaFilled(false);
        _OK.setPressedIcon(new ImageIcon("img/login_bt_hv.png"));
        _OK.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		//button event
         		
         		dispose();
         	}
        });
        
        _layeredPane.add(_OK);
        _layeredPane.add(_panel);
        
        getContentPane().add(_layeredPane);          
        setVisible(true);
	}
	
}
