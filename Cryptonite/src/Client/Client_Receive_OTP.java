package Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
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
	
	private boolean check=false;
	
	private Font font = new Font ("SansSerif", Font.BOLD,20);

	private String OTP;
	private Client_FileShare_Receive _cfr = null;
	
	public static void main(String args[])
	{
		new  Client_Receive_OTP();
	}

	 private void showMessage(String title, String message) {
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	
	public  Client_Receive_OTP(){
		_cfr = new Client_FileShare_Receive();
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(500,300,460,550);
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
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 460, 500);
        
        OTPField = new JTextField();
        OTPField.setText("Type otp number");
        OTPField.setBounds(158, 247, 254, 50);
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
        
        
        Select = new JButton(new ImageIcon("img/Select.png"));		
        Select.setPressedIcon(new ImageIcon("img/Selectp.png"));
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
        
        Check = new JButton(new ImageIcon("img/Check.png"));		
        Check.setPressedIcon(new ImageIcon("img/Checkp.png"));
        Check.setBounds(116, 410, 80, 40);
        Check.setBorderPainted(false);
        Check.setFocusPainted(false);
        Check.setContentAreaFilled(false);
        Check.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent arg0) 
			 {	
				check = _cfr.receiveFiles(OTP);
				if(check){
					showMessage("success", "ok!");
					dispose();
				}
				else{
					showMessage("fail", "fail..");
				}
			 }
		 });
        layeredPane.add(Check);
        
        Cancel = new JButton(new ImageIcon("img/Cancel.png"));		
        Cancel.setPressedIcon(new ImageIcon("img/Cancelp.png"));
        Cancel.setBounds(254, 410, 80,40);
        Cancel.setBorderPainted(false);
        Cancel.setFocusPainted(false);
        Cancel.setContentAreaFilled(false);
        Cancel.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent arg0) {
				dispose();
			 }
		 });
        layeredPane.add(Cancel);
        
        
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
        
        
	}
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
       }
   }
	
}

