package Client;

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
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.border.BevelBorder;



public class Client_Receive_OTP extends JFrame{
	
	private BufferedImage img = null;
	
	private JTextField OTPField;
	private JButton Select;
	private JButton Check;
	private JButton Cancel;
	private Font font = new Font ("SansSerif", Font.BOLD,20);

	private String OTP;
	private static boolean _flag=false;
	
	private Client_FileShare_Receive _cfr = null;
	
	public static void main(String args[])
	{
		new  Client_Receive_OTP();
	}

	
	public  Client_Receive_OTP(){
		
		_cfr = new Client_FileShare_Receive();
		
		try{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}	
		
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(500,300,460,550);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 470, 550);
        layeredPane.setLayout(null);
        
        try {
            img = ImageIO.read(new File("img/Filereceivebg.png"));
        } catch (IOException e) 
        {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 460, 500);
        
        OTPField = new JTextField();
        OTPField.setText("Type otp number");
        OTPField.setBounds(140, 244, 254, 50);
        OTPField.setForeground(Color.black);        
        OTPField.setFont(font);
        OTPField.setOpaque(false);
        OTPField.setBorder(BorderFactory.createEmptyBorder());
        OTPField.setHorizontalAlignment(JTextField.CENTER);
        OTPField.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) 
     		{
     			OTP = OTPField.getText();
     			_cfr.setOTP(OTP);
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
           });
        	OTPField.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		OTPField.setText("");
         	}
         });
        layeredPane.add(OTPField);
        
        
        Select = new JButton(new ImageIcon("img/select.png"));		
        Select.setRolloverIcon(new ImageIcon("img/selectR.png"));
        Select.setBounds(324, 328, 80, 40);
        Select.setBorderPainted(false);
        Select.setFocusPainted(false);
        Select.setContentAreaFilled(false);
        Select.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent arg0) 
			 {
				_cfr.folderSelect();
			 }
		 });
        layeredPane.add(Select);
        
        Check = new JButton(new ImageIcon("img/DOWNLOAD.png"));		
        Check.setRolloverIcon(new ImageIcon("img/DOWNLOADR.png"));
        Check.setBounds(116, 410, 80, 40);
        Check.setBorderPainted(false);
        Check.setFocusPainted(false);
        Check.setContentAreaFilled(false);
        Check.addActionListener(new ActionListener() 
        {
			 public void actionPerformed(ActionEvent arg0) 
			 {
				 _cfr.start();
				 _flag = true;
			 }
		 });
        layeredPane.add(Check);
        
        Cancel = new JButton(new ImageIcon("img/_cancel.png"));		
        Cancel.setRolloverIcon(new ImageIcon("img/_cancelR.png"));
        Cancel.setBounds(254, 410, 80,40);
        Cancel.setBorderPainted(false);
        Cancel.setFocusPainted(false);
        Cancel.setContentAreaFilled(false);
        Cancel.addActionListener(new ActionListener() 
        {
			 public void actionPerformed(ActionEvent arg0) 
			 {
				dispose();
			 }
		 });
        layeredPane.add(Cancel);
        
        
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
        
        
	}
	public static boolean send_flag(){
		return _flag;
	}
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
       }
   }
	
}

