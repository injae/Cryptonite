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

import Client.Client_TestOTP.MyPanel;

public class Client_TestGroupName extends JFrame{
	public static void main(String[] args){
		new Client_TestGroupName();
	}
	
	BufferedImage _img = null;
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(_img, 0, 0, null);
        }
    }
	JButton _OK;   
	
	JTextField _NameField;
		
/*	Client_Testmain test=new Client_Testmain();*/
	
	String Name;
	
	public Client_TestGroupName(){
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
        
        _NameField = new JTextField(15);
        _NameField.setBounds(190, 70, 200, 31);
        _NameField.setOpaque(true);
        _NameField.setForeground(Color.BLACK);
        _NameField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        _NameField.setHorizontalAlignment(JTextField.CENTER);
        _NameField.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) {
     			Name = _NameField.getText();
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
           });
        	_NameField.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		 _NameField.setText("");
         	}
         });
        _layeredPane.add( _NameField);
        
        _OK = new JButton(new ImageIcon("img/test_ok.png"));//input buttonimage
        _OK.setBounds(100, 150, 400, 50);
        _OK.setBorderPainted(false);
        _OK.setFocusPainted(false);
        _OK.setContentAreaFilled(false);
        _OK.setPressedIcon(new ImageIcon("img/test_ok_hv.png"));
        _OK.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		System.out.println("OTP : "+Name);
         		new Client_FileShare_Receive().receiveFiles(Name);
         		dispose();
         	}
        });
        
        _layeredPane.add(_OK);
        _layeredPane.add(_panel);
        
        getContentPane().add(_layeredPane);          
        setVisible(true);
	}
}
